package com.example.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

data class DiscoveredDevice(
    val id: String,
    val name: String,
    val rssi: Int,
    val firmware: String,
    val isConnected: Boolean = false,
    val batteryLevel: Int = 85
)

data class LiveVitals(
    val heartRate: Int = 76,
    val gsrMicrosiemens: Float = 4.2f,
    val skinTempCelsius: Float = 36.6f,
    val accelMagnitude: Float = 0.12f,
    val stressScore: Int = 54, // 0-100
    val stressCategory: StressCategory = StressCategory.MODERATE,
    val aiInsight: String = "Slight sympathetic autonomic arousal detected. Consider a 2-min breathing pause.",
    val packetsReceived: Long = 1840L,
    val samplingRateHz: Int = 50,
    val batteryPercent: Int = 88,
    val isCharging: Boolean = false,
    val sessionDurationSeconds: Long = 0L,
    val wearableOledText: String = "StressCare Band 2.0\nHR: 76 bpm  GSR: 4.2µS\nStatus: MODERATE [54%]"
)

enum class StressCategory(val label: String, val threshold: String) {
    LOW("Low Stress", "0 - 39%"),
    MODERATE("Moderate Stress", "40 - 69%"),
    HIGH("High Stress", "70 - 100%")
}

class BleDeviceManager(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

    private val _isBluetoothEnabled = MutableStateFlow(bluetoothAdapter?.isEnabled == true)
    val isBluetoothEnabled: StateFlow<Boolean> = _isBluetoothEnabled.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _isConnected = MutableStateFlow(true) // Connected to StressCare Band by default
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _connectedDevice = MutableStateFlow<DiscoveredDevice?>(
        DiscoveredDevice(
            id = "SC-ESP32-84F2",
            name = "StressCare Band 2.0",
            rssi = -58,
            firmware = "v2.4.1",
            isConnected = true,
            batteryLevel = 88
        )
    )
    val connectedDevice: StateFlow<DiscoveredDevice?> = _connectedDevice.asStateFlow()

    private val _discoveredDevices = MutableStateFlow<List<DiscoveredDevice>>(
        listOf(
            DiscoveredDevice("SC-ESP32-84F2", "StressCare Band 2.0", -58, "v2.4.1", isConnected = true, batteryLevel = 88),
            DiscoveredDevice("SC-ESP32-99B1", "StressCare Band 3.0 (Pro)", -52, "v3.0.2", isConnected = false, batteryLevel = 94),
            DiscoveredDevice("SC-ESP32-11C4", "StressCare Lab Sensor", -74, "v2.1.0", isConnected = false, batteryLevel = 65)
        )
    )
    val discoveredDevices: StateFlow<List<DiscoveredDevice>> = _discoveredDevices.asStateFlow()

    private val _liveVitals = MutableStateFlow(LiveVitals())
    val liveVitals: StateFlow<LiveVitals> = _liveVitals.asStateFlow()

    // Sliding buffers for sparkline waveforms (last 25 points)
    private val _hrWaveform = MutableStateFlow<List<Float>>(List(25) { 74f + sin(it.toFloat() * 0.5f) * 4f })
    val hrWaveform: StateFlow<List<Float>> = _hrWaveform.asStateFlow()

    private val _gsrWaveform = MutableStateFlow<List<Float>>(List(25) { 3.8f + sin(it.toFloat() * 0.3f) * 0.6f })
    val gsrWaveform: StateFlow<List<Float>> = _gsrWaveform.asStateFlow()

    private val _tempWaveform = MutableStateFlow<List<Float>>(List(25) { 36.5f + (it % 5) * 0.05f })
    val tempWaveform: StateFlow<List<Float>> = _tempWaveform.asStateFlow()

    private val _motionWaveform = MutableStateFlow<List<Float>>(List(25) { 0.1f + (it % 4) * 0.04f })
    val motionWaveform: StateFlow<List<Float>> = _motionWaveform.asStateFlow()

    private var streamingJob: Job? = null
    private var isMonitoringActive = true
    private var sessionSeconds = 480L
    private var packetCount = 1840L

    init {
        startStreamingSimulation()
    }

    fun startScan() {
        _isScanning.value = true
        scope.launch {
            delay(3500)
            _isScanning.value = false
        }
    }

    fun stopScan() {
        _isScanning.value = false
    }

    fun connectDevice(device: DiscoveredDevice) {
        scope.launch {
            _isConnected.value = false
            delay(800)
            _connectedDevice.value = device.copy(isConnected = true)
            _isConnected.value = true
            val updated = _discoveredDevices.value.map {
                it.copy(isConnected = (it.id == device.id))
            }
            _discoveredDevices.value = updated
        }
    }

    fun disconnectDevice() {
        _isConnected.value = false
        _connectedDevice.value = null
        val updated = _discoveredDevices.value.map { it.copy(isConnected = false) }
        _discoveredDevices.value = updated
    }

    fun setMonitoringActive(active: Boolean) {
        isMonitoringActive = active
        if (active && (streamingJob == null || streamingJob?.isActive == false)) {
            startStreamingSimulation()
        }
    }

    fun toggleMonitoring(): Boolean {
        isMonitoringActive = !isMonitoringActive
        return isMonitoringActive
    }

    private fun startStreamingSimulation() {
        streamingJob?.cancel()
        streamingJob = scope.launch(Dispatchers.Default) {
            var step = 0
            while (isActive) {
                delay(1000)
                if (!isMonitoringActive || !_isConnected.value) continue

                step++
                sessionSeconds++
                packetCount += 50 // 50 Hz sampling

                // Physiological signal generation
                val baseSine = sin(step * 0.15)
                val noiseHR = (Random.nextFloat() - 0.5f) * 4f
                val hr = (75 + (baseSine * 8) + noiseHR).toInt().coerceIn(58, 125)

                val noiseGSR = (Random.nextFloat() - 0.5f) * 0.3f
                val gsr = (4.0f + (baseSine.toFloat() * 1.2f) + noiseGSR).coerceIn(1.5f, 9.8f)

                val temp = (36.5f + (sin(step * 0.05).toFloat() * 0.3f) + (Random.nextFloat() * 0.08f)).coerceIn(35.8f, 37.6f)

                val accel = (0.08f + if (Random.nextFloat() > 0.85f) Random.nextFloat() * 0.4f else Random.nextFloat() * 0.05f).coerceIn(0.02f, 1.2f)

                // AI Stress Estimation algorithm
                // Normalized factors
                val hrNorm = ((hr - 60f) / 50f).coerceIn(0f, 1f)
                val gsrNorm = ((gsr - 2.0f) / 6.0f).coerceIn(0f, 1f)
                val tempFactor = ((37.2f - temp) / 1.0f).coerceIn(0f, 1f) // Peripheral vasoconstriction lowers skin temp during stress
                val motionFactor = accel.coerceIn(0f, 1f)

                val rawStress = (0.35f * hrNorm + 0.45f * gsrNorm + 0.10f * tempFactor + 0.10f * (1f - motionFactor)) * 100f
                val stressScore = rawStress.toInt().coerceIn(10, 95)

                val category = when {
                    stressScore < 40 -> StressCategory.LOW
                    stressScore < 70 -> StressCategory.MODERATE
                    else -> StressCategory.HIGH
                }

                val insight = when (category) {
                    StressCategory.LOW -> "Autonomic state is well-balanced. Heart rate variability is optimal."
                    StressCategory.MODERATE -> "Mild sympathetic arousal observed. Skin conductance slightly elevated."
                    StressCategory.HIGH -> "High sympathetic load detected! Rapid pulse & GSR spike. Immediate intervention advised."
                }

                val oledText = "StressCare Band 2.0\nHR: $hr bpm  GSR: ${String.format("%.1f", gsr)}µS\nStatus: ${category.name} [$stressScore%]"

                _liveVitals.value = LiveVitals(
                    heartRate = hr,
                    gsrMicrosiemens = (gsr * 10f).toInt() / 10f,
                    skinTempCelsius = (temp * 10f).toInt() / 10f,
                    accelMagnitude = (accel * 100f).toInt() / 100f,
                    stressScore = stressScore,
                    stressCategory = category,
                    aiInsight = insight,
                    packetsReceived = packetCount,
                    samplingRateHz = 50,
                    batteryPercent = 88,
                    isCharging = false,
                    sessionDurationSeconds = sessionSeconds,
                    wearableOledText = oledText
                )

                // Update sliding waveform buffers
                _hrWaveform.value = (_hrWaveform.value.drop(1) + hr.toFloat())
                _gsrWaveform.value = (_gsrWaveform.value.drop(1) + gsr)
                _tempWaveform.value = (_tempWaveform.value.drop(1) + temp)
                _motionWaveform.value = (_motionWaveform.value.drop(1) + accel)
            }
        }
    }
}
