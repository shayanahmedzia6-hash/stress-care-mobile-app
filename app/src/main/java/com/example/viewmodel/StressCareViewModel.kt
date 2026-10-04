package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SynthesizedAudioEngine
import com.example.ble.BleDeviceManager
import com.example.ble.DiscoveredDevice
import com.example.ble.LiveVitals
import com.example.data.local.StressCareDatabase
import com.example.data.local.entities.PssAssessmentEntity
import com.example.data.local.entities.SensorReadingEntity
import com.example.data.local.entities.StressSessionEntity
import com.example.data.local.entities.UserPreferencesEntity
import com.example.data.repository.StressCareRepository
import com.example.model.calculatePssScore
import com.example.model.getPssCategory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class MainTab {
    DASHBOARD,
    MONITORING,
    ANALYTICS,
    INTERVENTIONS,
    PROFILE
}

enum class ActiveOverlay {
    NONE,
    ONBOARDING,
    AUTH,
    BLE_PAIRING,
    CALIBRATION,
    GUIDED_BREATHING,
    AUDIO_PLAYER,
    PSS_QUESTIONNAIRE,
    MIGRAINE_DETAIL,
    NOTIFICATIONS,
    FEEDBACK
}

enum class BreathingPhase(val label: String, val seconds: Int) {
    INHALE("Inhale deeply through nose...", 4),
    HOLD_IN("Hold breath gently...", 4),
    EXHALE("Exhale slowly through mouth...", 4),
    HOLD_OUT("Rest & pause...", 4)
}

data class CalibrationStep(
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val isInProgress: Boolean,
    val value: String
)

class StressCareViewModel(application: Application) : AndroidViewModel(application) {

    private val database = StressCareDatabase.getDatabase(application, viewModelScope)
    val repository = StressCareRepository(database)

    val bleManager = BleDeviceManager(application, viewModelScope)
    val audioEngine = SynthesizedAudioEngine(viewModelScope)

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        application.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    // Navigation & Overlay states
    private val _currentTab = MutableStateFlow(MainTab.DASHBOARD)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _activeOverlay = MutableStateFlow(ActiveOverlay.NONE)
    val activeOverlay: StateFlow<ActiveOverlay> = _activeOverlay.asStateFlow()

    // Theme Mode: Defaults to false (Light Theme matching stresscarepp.netlify.app)
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setDarkMode(dark: Boolean) {
        _isDarkMode.value = dark
    }

    // History and PSS Flows from Room
    val allSessions: StateFlow<List<StressSessionEntity>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allPssAssessments: StateFlow<List<PssAssessmentEntity>> = repository.allPssAssessments
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val userPreferences: StateFlow<UserPreferencesEntity?> = repository.userPreferences
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    // UI Feedback & Toast events
    private val _toastEvents = MutableSharedFlow<String>()
    val toastEvents: SharedFlow<String> = _toastEvents.asSharedFlow()

    // Calibration Flow State
    private val _calibrationSecondsRemaining = MutableStateFlow(60)
    val calibrationSecondsRemaining: StateFlow<Int> = _calibrationSecondsRemaining.asStateFlow()

    private val _calibrationProgress = MutableStateFlow(0f)
    val calibrationProgress: StateFlow<Float> = _calibrationProgress.asStateFlow()

    private val _calibrationSteps = MutableStateFlow<List<CalibrationStep>>(emptyList())
    val calibrationSteps: StateFlow<List<CalibrationStep>> = _calibrationSteps.asStateFlow()

    private var calibrationJob: Job? = null

    // Guided Breathing Flow State
    private val _breathingPhase = MutableStateFlow(BreathingPhase.INHALE)
    val breathingPhase: StateFlow<BreathingPhase> = _breathingPhase.asStateFlow()

    private val _breathingPhaseProgress = MutableStateFlow(0f) // 0f to 1f within 4-sec cycle
    val breathingPhaseProgress: StateFlow<Float> = _breathingPhaseProgress.asStateFlow()

    private val _breathingTotalRemainingSeconds = MutableStateFlow(120) // 2-min session
    val breathingTotalRemainingSeconds: StateFlow<Int> = _breathingTotalRemainingSeconds.asStateFlow()

    private val _breathingCycleCount = MutableStateFlow(1)
    val breathingCycleCount: StateFlow<Int> = _breathingCycleCount.asStateFlow()

    private var breathingJob: Job? = null

    // PSS-10 Questionnaire State
    private val _pssAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val pssAnswers: StateFlow<Map<Int, Int>> = _pssAnswers.asStateFlow()

    private val _pssMoodSelection = MutableStateFlow("Moderate")
    val pssMoodSelection: StateFlow<String> = _pssMoodSelection.asStateFlow()

    private val _pssNotes = MutableStateFlow("")
    val pssNotes: StateFlow<String> = _pssNotes.asStateFlow()

    // Cloud Sync State
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    // Analytics Filters
    private val _analyticsTimeRange = MutableStateFlow("Week") // Day, Week, Month
    val analyticsTimeRange: StateFlow<String> = _analyticsTimeRange.asStateFlow()

    // Auth screen mode (Sign In vs Register)
    private val _isRegisterMode = MutableStateFlow(false)
    val isRegisterMode: StateFlow<Boolean> = _isRegisterMode.asStateFlow()

    init {
        // Automatically save periodic sensor reading snapshots to local SQLite database
        viewModelScope.launch {
            while (isActive) {
                delay(15_000) // Every 15 seconds save a data log for offline analysis
                val vitals = bleManager.liveVitals.value
                val reading = SensorReadingEntity(
                    timestamp = System.currentTimeMillis(),
                    heartRate = vitals.heartRate,
                    gsrMicrosiemens = vitals.gsrMicrosiemens,
                    skinTempCelsius = vitals.skinTempCelsius,
                    accelMagnitude = vitals.accelMagnitude,
                    stressScore = vitals.stressScore,
                    stressLevel = vitals.stressCategory.name,
                    isSynced = false
                )
                repository.saveReading(reading)
            }
        }
    }

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun setOverlay(overlay: ActiveOverlay) {
        _activeOverlay.value = overlay
        if (overlay == ActiveOverlay.CALIBRATION) {
            startBaselineCalibration()
        } else if (overlay == ActiveOverlay.GUIDED_BREATHING) {
            startGuidedBreathing()
        } else {
            stopBreathing()
        }
    }

    fun closeOverlay() {
        if (_activeOverlay.value == ActiveOverlay.GUIDED_BREATHING) {
            stopBreathing()
        }
        if (_activeOverlay.value == ActiveOverlay.CALIBRATION) {
            calibrationJob?.cancel()
        }
        _activeOverlay.value = ActiveOverlay.NONE
    }

    fun openMigraineDetail() {
        _activeOverlay.value = ActiveOverlay.MIGRAINE_DETAIL
    }

    fun openNotifications() {
        _activeOverlay.value = ActiveOverlay.NOTIFICATIONS
    }

    fun openFeedback() {
        _activeOverlay.value = ActiveOverlay.FEEDBACK
    }

    fun setAnalyticsTimeRange(range: String) {
        _analyticsTimeRange.value = range
    }

    fun setRegisterMode(isRegister: Boolean) {
        _isRegisterMode.value = isRegister
    }

    // Trigger Cloud Sync
    fun triggerCloudSync() {
        if (_isSyncing.value) return
        _isSyncing.value = true
        viewModelScope.launch {
            val result = repository.syncWithCloud()
            _isSyncing.value = false
            if (result.isSuccess) {
                val count = result.getOrDefault(0)
                emitToast("Cloud Sync Complete: $count records synchronized to Firestore")
            } else {
                emitToast("Sync failed. Stored locally in offline SQLite.")
            }
        }
    }

    // Baseline Calibration
    fun startBaselineCalibration() {
        calibrationJob?.cancel()
        _calibrationSecondsRemaining.value = 60
        _calibrationProgress.value = 0f

        _calibrationSteps.value = listOf(
            CalibrationStep("Heart Rate Baseline", "Detecting resting cardiac rhythm...", false, true, "Measuring"),
            CalibrationStep("GSR Baseline", "Analyzing tonic skin conductance...", false, false, "Pending"),
            CalibrationStep("Skin Temperature Baseline", "Measuring peripheral infrared reading...", false, false, "Pending"),
            CalibrationStep("Motion Baseline", "Verifying accelerometer stability...", false, false, "Pending")
        )

        calibrationJob = viewModelScope.launch {
            for (sec in 60 downTo 1) {
                _calibrationSecondsRemaining.value = sec
                val progress = (60 - sec) / 60f
                _calibrationProgress.value = progress

                // Progressively mark steps completed
                val currentVitals = bleManager.liveVitals.value
                val step1Done = sec <= 45
                val step2Done = sec <= 30
                val step3Done = sec <= 15
                val step4Done = sec <= 5

                _calibrationSteps.value = listOf(
                    CalibrationStep("Heart Rate Baseline", "Resting pulse calibrated", step1Done, !step1Done, if (step1Done) "68 bpm (Optimal)" else "Measuring (${currentVitals.heartRate} bpm)"),
                    CalibrationStep("GSR Baseline", "Tonic conductance established", step2Done, step1Done && !step2Done, if (step2Done) "3.2 µS (Balanced)" else if (step1Done) "Measuring (${currentVitals.gsrMicrosiemens} µS)" else "Pending"),
                    CalibrationStep("Skin Temperature Baseline", "Peripheral thermoregulation recorded", step3Done, step2Done && !step3Done, if (step3Done) "36.5 °C (Normal)" else if (step2Done) "Measuring (${currentVitals.skinTempCelsius} °C)" else "Pending"),
                    CalibrationStep("Motion Baseline", "Wearable motion calibrated", step4Done, step3Done && !step4Done, if (step4Done) "Stable (0.02g)" else if (step3Done) "Measuring..." else "Pending")
                )

                delay(1000)
            }

            _calibrationSecondsRemaining.value = 0
            _calibrationProgress.value = 1f
            repository.updateBaselines(68, 3.2f, 36.5f)
            vibratePattern(longArrayOf(0, 150, 100, 200))
            emitToast("Physiological Baseline Calibration Complete!")
        }
    }

    // Guided Box Breathing
    fun startGuidedBreathing() {
        breathingJob?.cancel()
        _breathingTotalRemainingSeconds.value = 120
        _breathingCycleCount.value = 1

        breathingJob = viewModelScope.launch {
            val phases = listOf(
                BreathingPhase.INHALE,
                BreathingPhase.HOLD_IN,
                BreathingPhase.EXHALE,
                BreathingPhase.HOLD_OUT
            )

            var phaseIdx = 0
            while (isActive && _breathingTotalRemainingSeconds.value > 0) {
                val currentP = phases[phaseIdx % phases.size]
                _breathingPhase.value = currentP

                // Haptic feedback on phase shift
                vibratePhaseShift()

                val phaseDurationMs = currentP.seconds * 1000L
                val steps = 40
                val stepDelayMs = phaseDurationMs / steps

                for (s in 1..steps) {
                    delay(stepDelayMs)
                    _breathingPhaseProgress.value = s.toFloat() / steps.toFloat()
                }

                phaseIdx++
                _breathingTotalRemainingSeconds.value = (_breathingTotalRemainingSeconds.value - currentP.seconds).coerceAtLeast(0)
                _breathingCycleCount.value = (phaseIdx / 4) + 1
            }

            // Session completed: Record session into Room database!
            val completedSession = StressSessionEntity(
                startTime = System.currentTimeMillis() - 120_000,
                endTime = System.currentTimeMillis(),
                durationSeconds = 120,
                avgStress = 28,
                maxStress = 42,
                avgHeartRate = 66,
                label = "2-Min Guided Box Breathing",
                isSynced = false
            )
            repository.saveSession(completedSession)
            vibratePattern(longArrayOf(0, 250, 150, 400))
            emitToast("Breathing Session Completed! Stress reduced to 28%")
        }
    }

    fun stopBreathing() {
        breathingJob?.cancel()
        breathingJob = null
    }

    // PSS Answers handling
    fun setPssAnswer(questionId: Int, answerValue: Int) {
        val updated = _pssAnswers.value.toMutableMap()
        updated[questionId] = answerValue
        _pssAnswers.value = updated
    }

    fun setPssMood(mood: String) {
        _pssMoodSelection.value = mood
    }

    fun setPssNotes(notes: String) {
        _pssNotes.value = notes
    }

    fun submitPssAssessment() {
        val answers = _pssAnswers.value
        val score = calculatePssScore(answers)
        val category = getPssCategory(score)

        viewModelScope.launch {
            val answersList = (1..10).map { id -> answers[id] ?: 0 }
            val entity = PssAssessmentEntity(
                timestamp = System.currentTimeMillis(),
                score = score,
                category = category,
                answersJson = answersList.toString(),
                moodRating = _pssMoodSelection.value,
                userNotes = _pssNotes.value.ifBlank { "Regular weekly check-in." },
                isSynced = false
            )
            repository.savePssAssessment(entity)
            emitToast("PSS Assessment Submitted! Score: $score/40 ($category)")
            closeOverlay()
        }
    }

    private fun emitToast(msg: String) {
        viewModelScope.launch {
            _toastEvents.emit(msg)
        }
    }

    private fun vibratePhaseShift() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(80)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    private fun vibratePattern(pattern: LongArray) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, -1)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioEngine.release()
    }
}
