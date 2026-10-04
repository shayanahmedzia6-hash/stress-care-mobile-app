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
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.shadow
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
import com.example.ui.theme.StatusOnlineGreen
import com.example.ui.theme.StressHighColor
import com.example.ui.theme.StressLowColor
import com.example.ui.theme.StressModerateColor
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
    val isDarkMode by viewModel.isDarkMode.collectAsState()

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

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0B0F19)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFDEEEFF), Color(0xFFF0F6FF), Color(0xFFF5F8FF)))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("monitoring_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Session Telemetry Bar
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                                        .background(StatusOnlineGreen)
                                )
                                Text(
                                    text = "ACTIVE BLE SESSION",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = StatusOnlineGreen,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                            Text(
                                text = formattedDuration,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${liveVitals.packetsReceived} packets",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${liveVitals.samplingRateHz} Hz Sampling",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF2563EB),
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BatteryChargingFull,
                                    contentDescription = "Battery",
                                    tint = StatusOnlineGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${liveVitals.batteryPercent}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // 2. Large Interactive Stress Gauge Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                        .testTag("monitoring_stress_circle"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, stressColor.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "CONTINUOUS AI STRESS INFERENCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(190.dp)
                        ) {
                            // Ambient glowing background halo
                            Box(
                                modifier = Modifier
                                    .size(180.dp * pulseSize)
                                    .clip(CircleShape)
                                    .background(stressColor.copy(alpha = 0.08f))
                            )

                            // Circular Progress
                            CircularProgressIndicator(
                                progress = { 1f },
                                modifier = Modifier.size(176.dp),
                                color = if (isDarkMode) Color(0xFF334155) else Color(0xFFDCE8F8),
                                strokeWidth = 14.dp,
                                strokeCap = StrokeCap.Round
                            )
                            CircularProgressIndicator(
                                progress = { (liveVitals.stressScore / 100f).coerceIn(0.05f, 1f) },
                                modifier = Modifier.size(176.dp),
                                color = stressColor,
                                strokeWidth = 14.dp,
                                strokeCap = StrokeCap.Round
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${liveVitals.stressScore}%",
                                    style = MaterialTheme.typography.displayMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(stressColor.copy(alpha = 0.15f))
                                        .border(1.dp, stressColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
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

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = liveVitals.aiInsight,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            lineHeight = 20.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            // 3. Wearable Display Sync (ESP32-S3 OLED Simulation)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp))
                        .testTag("monitoring_oled_sync"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
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
                                    tint = Color(0xFF2563EB),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "WEARABLE DISPLAY MIRROR",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF2563EB),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "ESP32-S3 0.96\" OLED",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 9.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Simulated Wearable OLED display preview (Clean light-mode ice-blue or dark-mode slate)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    1.dp,
                                    if (isDarkMode) Color(0xFF1E293B) else Color(0xFFD0DFF0),
                                    RoundedCornerShape(12.dp)
                                ),
                            color = if (isDarkMode) Color(0xFF0F172A) else Color(0xFFF0F6FF)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(StatusOnlineGreen)
                                        )
                                        Text(
                                            text = "STRESSCARE v2.4",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            color = if (isDarkMode) Color(0xFF38BDF8) else Color(0xFF1A2B4B),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = "BAT: ${liveVitals.batteryPercent}%",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = StatusOnlineGreen,
                                        fontWeight = FontWeight.Bold
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
                                        color = ColorHeartRate,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "GSR: ${String.format("%.1f", liveVitals.gsrMicrosiemens)} µS",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 13.sp,
                                        color = if (isDarkMode) Color(0xFF60A5FA) else ColorGSR,
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
                                            StressCategory.LOW -> StatusOnlineGreen
                                            StressCategory.MODERATE -> StressModerateColor
                                            StressCategory.HIGH -> StressHighColor
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "T: ${String.format("%.1f", liveVitals.skinTempCelsius)}°C",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = if (isDarkMode) Color(0xFFFDBA74) else ColorSkinTemp,
                                        fontWeight = FontWeight.Bold
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    readingValue = "${String.format("%.1f", liveVitals.gsrMicrosiemens)} µS",
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
                    readingValue = "${String.format("%.1f", liveVitals.skinTempCelsius)} °C",
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
                    readingValue = "${String.format("%.2f", liveVitals.accelMagnitude)} g",
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.setOverlay(ActiveOverlay.CALIBRATION) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        color = MaterialTheme.colorScheme.onSurface,
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                SparklineWaveform(
                    dataPoints = waveform,
                    lineColor = accentColor,
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 3f
                )
            }

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}
