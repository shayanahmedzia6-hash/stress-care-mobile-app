package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ble.StressCategory
import com.example.ui.components.SparklineWaveform
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.ColorGSR
import com.example.ui.theme.ColorHeartRate
import com.example.ui.theme.ColorMotion
import com.example.ui.theme.ColorSkinTemp
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceHighlight
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
import com.example.viewmodel.MainTab
import com.example.viewmodel.StressCareViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: StressCareViewModel,
    modifier: Modifier = Modifier
) {
    val liveVitals by viewModel.bleManager.liveVitals.collectAsState()
    val connectedDevice by viewModel.bleManager.connectedDevice.collectAsState()
    val isConnected by viewModel.bleManager.isConnected.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()

    val hrWaveform by viewModel.bleManager.hrWaveform.collectAsState()
    val gsrWaveform by viewModel.bleManager.gsrWaveform.collectAsState()
    val tempWaveform by viewModel.bleManager.tempWaveform.collectAsState()
    val motionWaveform by viewModel.bleManager.motionWaveform.collectAsState()

    val currentDateStr = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = currentDateStr.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = CyanPrimary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Hello, Alex 👋",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.triggerCloudSync() },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkBorder, CircleShape)
                            .testTag("dashboard_sync_button")
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = CyanPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = "Sync Cloud",
                                tint = CyanPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(CyanPrimary, PurpleAccent))
                            )
                            .clickable { viewModel.selectTab(MainTab.PROFILE) }
                            .testTag("dashboard_profile_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AM",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // 2. Connected Device Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                    .clickable { viewModel.setOverlay(ActiveOverlay.BLE_PAIRING) }
                    .testTag("dashboard_device_card"),
                color = DarkSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CyanPrimary.copy(alpha = 0.15f))
                                .border(1.dp, CyanPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BluetoothConnected,
                                contentDescription = "BLE Band",
                                tint = CyanPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = connectedDevice?.name ?: "StressCare Band 2.0",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (isConnected) EmeraldAccent else Color.Gray)
                                )
                            }
                            Text(
                                text = if (isConnected) "Live Stream • ESP32-S3 BLE" else "Disconnected",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isConnected) EmeraldAccent else TextMuted
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BatteryChargingFull,
                            contentDescription = "Battery",
                            tint = EmeraldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${liveVitals.batteryPercent}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 3. Live Stress Circle Gauge Card
        item {
            val stressColor = when (liveVitals.stressCategory) {
                StressCategory.LOW -> StressLowColor
                StressCategory.MODERATE -> StressModerateColor
                StressCategory.HIGH -> StressHighColor
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, stressColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .testTag("dashboard_stress_gauge"),
                color = DarkSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "REAL-TIME STRESS LEVEL",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(stressColor.copy(alpha = 0.2f))
                                .border(1.dp, stressColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = liveVitals.stressCategory.label.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = stressColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Circular Stress Meter
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(170.dp)
                    ) {
                        // Background track
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.size(170.dp),
                            color = DarkBorder,
                            strokeWidth = 14.dp,
                            strokeCap = StrokeCap.Round
                        )
                        // Active colored progress
                        CircularProgressIndicator(
                            progress = { liveVitals.stressScore / 100f },
                            modifier = Modifier.size(170.dp),
                            color = stressColor,
                            strokeWidth = 14.dp,
                            strokeCap = StrokeCap.Round
                        )

                        // Center content
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${liveVitals.stressScore}%",
                                style = MaterialTheme.typography.displayMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Autonomic Index",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // AI Insight Box
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp)),
                        color = DarkSurfaceElevated
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "AI Tip",
                                tint = CyanPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = liveVitals.aiInsight,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // 4. Migraine Risk Summary Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                AmberWarning.copy(alpha = 0.15f),
                                DarkSurfaceElevated
                            )
                        )
                    )
                    .border(1.dp, AmberWarning.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .clickable { viewModel.selectTab(MainTab.ANALYTICS) }
                    .testTag("dashboard_migraine_banner"),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AmberWarning.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Migraine Risk",
                                tint = AmberWarning,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Migraine Risk: Medium (58/100)",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "Night HRV drop & GSR spikes detected. Tap for insights.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open Insights",
                        tint = AmberWarning,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 5. Section Title: Sensor Telemetry
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PHYSIOLOGICAL SENSORS",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "50 Hz Streaming",
                    style = MaterialTheme.typography.labelSmall,
                    color = CyanPrimary
                )
            }
        }

        // 6. 4 Metric Cards (2x2 Grid)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Metric 1: Heart Rate (MAX30102)
                    MetricCard(
                        title = "Heart Rate",
                        sensorModel = "MAX30102 PPG",
                        value = "${liveVitals.heartRate}",
                        unit = "BPM",
                        statusText = "Resting: 60-100",
                        accentColor = ColorHeartRate,
                        icon = Icons.Default.Favorite,
                        waveform = hrWaveform,
                        modifier = Modifier.weight(1f)
                    )

                    // Metric 2: GSR Conductance (Grove GSR)
                    MetricCard(
                        title = "Skin Conductance",
                        sensorModel = "Grove GSR",
                        value = "${liveVitals.gsrMicrosiemens}",
                        unit = "µS",
                        statusText = "Tonic: Moderate",
                        accentColor = ColorGSR,
                        icon = Icons.Default.Waves,
                        waveform = gsrWaveform,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Metric 3: Skin Temperature (MLX90614)
                    MetricCard(
                        title = "Skin Temp",
                        sensorModel = "MLX90614 IR",
                        value = "${liveVitals.skinTempCelsius}",
                        unit = "°C",
                        statusText = "Optimal: 36.5°",
                        accentColor = ColorSkinTemp,
                        icon = Icons.Default.Thermostat,
                        waveform = tempWaveform,
                        modifier = Modifier.weight(1f)
                    )

                    // Metric 4: Motion Activity (MPU6050)
                    MetricCard(
                        title = "Motion IMU",
                        sensorModel = "MPU6050 6-Axis",
                        value = "${liveVitals.accelMagnitude}",
                        unit = "g",
                        statusText = "Resting State",
                        accentColor = ColorMotion,
                        icon = Icons.Default.TrendingUp,
                        waveform = motionWaveform,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 7. Section Title: Quick Actions
        item {
            Text(
                text = "QUICK ACTIONS",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        // 8. Quick Actions Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionButton(
                        title = "Live Monitor",
                        subtitle = "ESP32 Realtime",
                        icon = Icons.Default.PlayArrow,
                        accentColor = CyanPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectTab(MainTab.MONITORING) }
                    )

                    QuickActionButton(
                        title = "Breathing",
                        subtitle = "Box 4-4-4-4",
                        icon = Icons.Default.Air,
                        accentColor = EmeraldAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setOverlay(ActiveOverlay.GUIDED_BREATHING) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionButton(
                        title = "Stress History",
                        subtitle = "7-Day Trends",
                        icon = Icons.Default.History,
                        accentColor = PurpleAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectTab(MainTab.ANALYTICS) }
                    )

                    QuickActionButton(
                        title = "Sleep & Migraine",
                        subtitle = "Risk Forecast",
                        icon = Icons.Default.BarChart,
                        accentColor = AmberWarning,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectTab(MainTab.ANALYTICS) }
                    )
                }

                QuickActionButton(
                    title = "Clinical PSS-10 Assessment",
                    subtitle = "10-Question Standardized Perceived Stress Scale",
                    icon = Icons.Default.RateReview,
                    accentColor = CyanPrimary,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.setOverlay(ActiveOverlay.PSS_QUESTIONNAIRE) }
                )
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    sensorModel: String,
    value: String,
    unit: String,
    statusText: String,
    accentColor: Color,
    icon: ImageVector,
    waveform: List<Float>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp)),
        color = DarkSurface
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
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = sensorModel,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 9.sp
                )
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Live Waveform Sparkline
            SparklineWaveform(
                dataPoints = waveform,
                lineColor = accentColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
            )

            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = DarkSurface
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}
