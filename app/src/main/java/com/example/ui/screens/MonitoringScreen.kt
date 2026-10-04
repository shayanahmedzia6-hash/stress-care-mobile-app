package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ble.StressCategory
import com.example.ui.components.SparklineWaveform
import com.example.ui.theme.ColorGSR
import com.example.ui.theme.ColorHeartRate
import com.example.ui.theme.ColorMotion
import com.example.ui.theme.ColorSkinTemp
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RosePulse
import com.example.ui.theme.StressHighColor
import com.example.ui.theme.StressLowColor
import com.example.ui.theme.StressModerateColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.ActiveOverlay
import com.example.viewmodel.StressCareViewModel

@Composable
fun MonitoringScreen(
    viewModel: StressCareViewModel,
    modifier: Modifier = Modifier
) {
    val liveVitals by viewModel.bleManager.liveVitals.collectAsState()
    val isConnected by viewModel.bleManager.isConnected.collectAsState()
    val connectedDevice by viewModel.bleManager.connectedDevice.collectAsState()

    val hrWaveform by viewModel.bleManager.hrWaveform.collectAsState()
    val gsrWaveform by viewModel.bleManager.gsrWaveform.collectAsState()
    val tempWaveform by viewModel.bleManager.tempWaveform.collectAsState()
    val motionWaveform by viewModel.bleManager.motionWaveform.collectAsState()

    val sessionMins = liveVitals.sessionDurationSeconds / 60
    val sessionSecs = liveVitals.sessionDurationSeconds % 60
    val formattedDuration = String.format("%02d:%02d", sessionMins, sessionSecs)

    val stressColor = when (liveVitals.stressCategory) {
        StressCategory.LOW -> StressLowColor
        StressCategory.MODERATE -> StressModerateColor
        StressCategory.HIGH -> StressHighColor
    }

    val infiniteTransition = rememberInfiniteTransition(label = "monitoring_pulse")
    val pulseSize by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseSize"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Session Telemetry Bar
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, DarkBorder, RoundedCornerShape(16.dp)),
                color = DarkSurface
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldAccent)
                            )
                            Text(
                                text = "ACTIVE BLE SESSION",
                                style = MaterialTheme.typography.labelSmall,
                                color = EmeraldAccent,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = formattedDuration,
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${liveVitals.packetsReceived} packets",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = "${liveVitals.samplingRateHz} Hz Sampling",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyanPrimary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BatteryChargingFull,
                                contentDescription = "Battery",
                                tint = EmeraldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${liveVitals.batteryPercent}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // 2. Large Interactive Stress Gauge
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, stressColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .testTag("monitoring_stress_circle"),
                color = DarkSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CONTINUOUS AI STRESS INFERENCE",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(200.dp)
                    ) {
                        // Ambient glowing background halo
                        Box(
                            modifier = Modifier
                                .size(190.dp * pulseSize)
                                .clip(CircleShape)
                                .background(stressColor.copy(alpha = 0.08f))
                        )

                        // Circular Progress
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.size(190.dp),
                            color = DarkBorder,
                            strokeWidth = 16.dp,
                            strokeCap = StrokeCap.Round
                        )
                        CircularProgressIndicator(
                            progress = { liveVitals.stressScore / 100f },
                            modifier = Modifier.size(190.dp),
                            color = stressColor,
                            strokeWidth = 16.dp,
                            strokeCap = StrokeCap.Round
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${liveVitals.stressScore}%",
                                style = MaterialTheme.typography.displayLarge,
                                color = TextPrimary,
                                fontWeight = FontWeight.Black
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(stressColor.copy(alpha = 0.2f))
                                    .border(1.dp, stressColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = liveVitals.stressCategory.label.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = stressColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = liveVitals.aiInsight,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // 3. Wearable Display Sync (ESP32-S3 OLED Simulation)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .testTag("monitoring_oled_sync"),
                color = Color(0xFF030712) // Deep OLED black
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.StayCurrentPortrait,
                                contentDescription = "Wearable OLED",
                                tint = CyanPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "WEARABLE DISPLAY SYNC (ESP32-S3)",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyanPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "0.96\" OLED MIRROR",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 9.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Simulated monochrome OLED display box
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp)),
                        color = Color(0xFF000511)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "STRESSCARE v2.4",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "BAT: ${liveVitals.batteryPercent}%",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = Color(0xFF34D399)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "♥ HR: ${liveVitals.heartRate} BPM",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    color = Color(0xFFFB7185),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "GSR: ${liveVitals.gsrMicrosiemens} µS",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    color = Color(0xFF60A5FA),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "STATUS: ${liveVitals.stressCategory.name} [${liveVitals.stressScore}%]",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = when (liveVitals.stressCategory) {
                                        StressCategory.LOW -> Color(0xFF34D399)
                                        StressCategory.MODERATE -> Color(0xFFFBBF24)
                                        StressCategory.HIGH -> Color(0xFFF87171)
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "T: ${liveVitals.skinTempCelsius}°C",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = Color(0xFFFDBA74)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Detailed Oscilloscope Sensor Metrics
        item {
            Text(
                text = "REAL-TIME SENSOR OSCILLOSCOPES",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        // Sensor 1: PPG Heart Rate
        item {
            SensorOscilloscopeCard(
                title = "Photoplethysmography (MAX30102 PPG)",
                readingValue = "${liveVitals.heartRate} BPM",
                subtitle = "Cardiac pulse waves with beat-to-beat variability extraction",
                accentColor = ColorHeartRate,
                waveform = hrWaveform,
                icon = Icons.Default.Favorite
            )
        }

        // Sensor 2: Galvanic Skin Response
        item {
            SensorOscilloscopeCard(
                title = "Galvanic Skin Response (Grove GSR)",
                readingValue = "${liveVitals.gsrMicrosiemens} µS",
                subtitle = "Electrodermal sympathetic conductance & sweat gland activation",
                accentColor = ColorGSR,
                waveform = gsrWaveform,
                icon = Icons.Default.Waves
            )
        }

        // Sensor 3: Skin Temperature
        item {
            SensorOscilloscopeCard(
                title = "Infrared Skin Temperature (MLX90614)",
                readingValue = "${liveVitals.skinTempCelsius} °C",
                subtitle = "Peripheral microvascular vasoconstriction tracking",
                accentColor = ColorSkinTemp,
                waveform = tempWaveform,
                icon = Icons.Default.Thermostat
            )
        }

        // Sensor 4: Accelerometer IMU
        item {
            SensorOscilloscopeCard(
                title = "6-Axis Motion / IMU (MPU6050)",
                readingValue = "${liveVitals.accelMagnitude} g",
                subtitle = "Body stillness & micro-tremor motion artifact compensation",
                accentColor = ColorMotion,
                waveform = motionWaveform,
                icon = Icons.Default.TrendingUp
            )
        }

        // 5. Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.setOverlay(ActiveOverlay.CALIBRATION) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Calibrate",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Calibrate")
                }

                Button(
                    onClick = { viewModel.setOverlay(ActiveOverlay.GUIDED_BREATHING) },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black)
                ) {
                    Icon(
                        imageVector = Icons.Default.Air,
                        contentDescription = "Box Breathing",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Box Breathing", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SensorOscilloscopeCard(
    title: String,
    readingValue: String,
    subtitle: String,
    accentColor: Color,
    waveform: List<Float>,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp)),
        color = DarkSurface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = readingValue,
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }

            // Real-Time Waveform Chart
            SparklineWaveform(
                dataPoints = waveform,
                lineColor = accentColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                strokeWidth = 3f
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
