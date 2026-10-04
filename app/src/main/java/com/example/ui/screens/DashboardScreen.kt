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
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Battery5Bar
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.shadow
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
import com.example.ui.theme.ColorGSR
import com.example.ui.theme.ColorGSRBgLight
import com.example.ui.theme.ColorHeartRate
import com.example.ui.theme.ColorHeartRateBgLight
import com.example.ui.theme.ColorMotion
import com.example.ui.theme.ColorMotionBgLight
import com.example.ui.theme.ColorSkinTemp
import com.example.ui.theme.ColorSkinTempBgLight
import com.example.ui.theme.MigraineBorderLight
import com.example.ui.theme.MigraineCardBgEnd
import com.example.ui.theme.MigraineCardBgStart
import com.example.ui.theme.MigraineTextLight
import com.example.ui.theme.StatusOnlineGreen
import com.example.ui.theme.StressHighColor
import com.example.ui.theme.StressLowColor
import com.example.ui.theme.StressModerateColor
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
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val hrWaveform by viewModel.bleManager.hrWaveform.collectAsState()
    val gsrWaveform by viewModel.bleManager.gsrWaveform.collectAsState()
    val tempWaveform by viewModel.bleManager.tempWaveform.collectAsState()
    val motionWaveform by viewModel.bleManager.motionWaveform.collectAsState()

    val currentDateStr = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
    val currentTimeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())

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

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0B0F19)))
    } else {
        Brush.verticalGradient(
            listOf(
                Color(0xFFDEEEFF),
                Color(0xFFF0F6FF),
                Color(0xFFF5F8FF)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("dashboard_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. Top Header: Greeting + Theme Switcher + Notifications + Avatar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = currentDateStr,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp
                        )
                        Text(
                            text = "Hello, Alex 👋",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Light / Dark Mode Toggle Button
                        IconButton(
                            onClick = { viewModel.toggleDarkMode() },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .testTag("dashboard_theme_toggle")
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = if (isDarkMode) Color(0xFFFBBF24) else Color(0xFF2563EB),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Notifications Bell Button
                        IconButton(
                            onClick = { viewModel.openNotifications() },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .testTag("dashboard_notifications_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // User Avatar
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2563EB))
                                .clickable { viewModel.selectTab(MainTab.PROFILE) }
                                .testTag("dashboard_profile_avatar"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AM",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // 2. Connected Wearable Card (matching stresscarepp.netlify.app)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .clickable { viewModel.setOverlay(ActiveOverlay.BLE_PAIRING) }
                        .testTag("dashboard_wearable_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkMode) Color(0xFF1E293B) else Color(0xFFF0F4FA)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BluetoothConnected,
                                    contentDescription = "Smart Band",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = connectedDevice?.name ?: "StressCare Band 2.0",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (isConnected) StatusOnlineGreen else Color.Gray)
                                    )
                                }
                                Text(
                                    text = if (isConnected) "Live Stream • ESP32-S3 BLE" else "Disconnected • Tap to scan",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isConnected) Color(0xFF2563EB) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.5.sp
                                )
                            }
                        }

                        // Battery & Signal Stats
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Battery5Bar,
                                    contentDescription = null,
                                    tint = StatusOnlineGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${liveVitals.batteryPercent}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SignalCellularAlt,
                                    contentDescription = null,
                                    tint = Color(0xFF2563EB),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${connectedDevice?.rssi ?: -62} dBm",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            // 3. Stress Level Card with Circular Gauge
            item {
                val stressScore = liveVitals.stressScore
                val stressCategory = liveVitals.stressCategory

                val statusLabel = when (stressCategory) {
                    StressCategory.LOW -> "Low Stress"
                    StressCategory.MODERATE -> "Moderate Stress"
                    StressCategory.HIGH -> "High Stress"
                }

                val statusColor = when (stressCategory) {
                    StressCategory.LOW -> StressLowColor
                    StressCategory.MODERATE -> StressModerateColor
                    StressCategory.HIGH -> StressHighColor
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                        .clickable { viewModel.selectTab(MainTab.MONITORING) }
                        .testTag("dashboard_stress_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFE8F3FF)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Gauge with percentage
                        Box(
                            modifier = Modifier.size(108.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { 1f },
                                modifier = Modifier.size(100.dp),
                                color = if (isDarkMode) Color(0xFF334155) else Color(0xFFDCE8F8),
                                strokeWidth = 10.dp,
                                strokeCap = StrokeCap.Round
                            )
                            CircularProgressIndicator(
                                progress = { (stressScore / 100f).coerceIn(0.05f, 1f) },
                                modifier = Modifier.size(100.dp),
                                color = statusColor,
                                strokeWidth = 10.dp,
                                strokeCap = StrokeCap.Round
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(
                                        text = "$stressScore",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 1.sp
                                    )
                                    Text(
                                        text = "%",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = "Autonomic Index",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 9.sp
                                )
                            }
                        }

                        // Stress Info
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Waves,
                                    contentDescription = null,
                                    tint = Color(0xFF2563EB),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = statusLabel,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = when (stressCategory) {
                                    StressCategory.LOW -> "Autonomic nervous system is balanced and restorative."
                                    StressCategory.MODERATE -> "Mild sympathetic arousal observed. Skin conductance slightly elevated."
                                    StressCategory.HIGH -> "Acute sympathetic stress state detected. Recommended brief breathing break."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Predicted from: HR, GSR & Temp",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF2563EB),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp
                            )
                        }
                    }
                }
            }

            // 4. 4 Metric Cards with Preserved Real-Time Sparkline Waveforms
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Metric 1: Heart Rate
                    DashboardMetricMiniCard(
                        title = "Heart Rate",
                        value = "${liveVitals.heartRate}",
                        unit = "BPM",
                        sub = "Resting: 60-100",
                        icon = Icons.Default.Favorite,
                        iconColor = ColorHeartRate,
                        iconBg = if (isDarkMode) ColorHeartRate.copy(alpha = 0.2f) else ColorHeartRateBgLight,
                        waveform = hrWaveform,
                        waveColor = ColorHeartRate,
                        modifier = Modifier.weight(1f)
                    )

                    // Metric 2: Skin Conductance
                    DashboardMetricMiniCard(
                        title = "Skin Cond.",
                        value = String.format(Locale.US, "%.1f", liveVitals.gsrMicrosiemens),
                        unit = "µS",
                        sub = "Tonic: Mod",
                        icon = Icons.Default.Waves,
                        iconColor = ColorGSR,
                        iconBg = if (isDarkMode) ColorGSR.copy(alpha = 0.2f) else ColorGSRBgLight,
                        waveform = gsrWaveform,
                        waveColor = ColorGSR,
                        modifier = Modifier.weight(1f)
                    )

                    // Metric 3: Skin Temp
                    DashboardMetricMiniCard(
                        title = "Skin Temp",
                        value = String.format(Locale.US, "%.1f", liveVitals.skinTempCelsius),
                        unit = "°C",
                        sub = "Normal",
                        icon = Icons.Default.Thermostat,
                        iconColor = ColorSkinTemp,
                        iconBg = if (isDarkMode) ColorSkinTemp.copy(alpha = 0.2f) else ColorSkinTempBgLight,
                        waveform = tempWaveform,
                        waveColor = ColorSkinTemp,
                        modifier = Modifier.weight(1f)
                    )

                    // Metric 4: Motion
                    DashboardMetricMiniCard(
                        title = "Motion",
                        value = String.format(Locale.US, "%.2f", liveVitals.accelMagnitude),
                        unit = "g",
                        sub = "Stillness",
                        icon = Icons.AutoMirrored.Filled.DirectionsRun,
                        iconColor = ColorMotion,
                        iconBg = if (isDarkMode) ColorMotion.copy(alpha = 0.2f) else ColorMotionBgLight,
                        waveform = motionWaveform,
                        waveColor = ColorMotion,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 5. Migraine Risk Card (matching stresscarepp.netlify.app)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(18.dp))
                        .clickable { viewModel.openMigraineDetail() }
                        .testTag("dashboard_migraine_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF064E3B) else MigraineCardBgStart
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDarkMode) Color(0xFF059669) else MigraineBorderLight
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isDarkMode) Color(0xFF059669) else Color(0xFFD1FAE5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = if (isDarkMode) Color.White else MigraineTextLight,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Migraine Risk: Medium (58/100)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkMode) Color.White else MigraineTextLight
                                )
                                Text(
                                    text = "Night HRV drop & GSR spikes detected. Tap for insights.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isDarkMode) Color(0xFFA7F3D0) else Color(0xFF059669),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View Details",
                            tint = if (isDarkMode) Color(0xFFA7F3D0) else MigraineTextLight,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // 6. Quick Actions Section (5 cards matching stresscarepp.netlify.app)
            item {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    QuickActionMiniCard(
                        title = "Live\nMonitor",
                        icon = Icons.Default.MonitorHeart,
                        iconColor = Color(0xFF2563EB),
                        bgColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFDBEAFE),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectTab(MainTab.MONITORING) }
                    )
                    QuickActionMiniCard(
                        title = "Box\nBreathing",
                        icon = Icons.Default.Air,
                        iconColor = Color(0xFF0D9488),
                        bgColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFCCFBF1),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setOverlay(ActiveOverlay.GUIDED_BREATHING) }
                    )
                    QuickActionMiniCard(
                        title = "History\nTrends",
                        icon = Icons.Default.History,
                        iconColor = Color(0xFF4F46E5),
                        bgColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFE0E7FF),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectTab(MainTab.ANALYTICS) }
                    )
                    QuickActionMiniCard(
                        title = "Sleep &\nMigraine",
                        icon = Icons.Default.BarChart,
                        iconColor = Color(0xFFCA8A04),
                        bgColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFFEF9C3),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openMigraineDetail() }
                    )
                    QuickActionMiniCard(
                        title = "Weekly\nPSS",
                        icon = Icons.Default.RateReview,
                        iconColor = Color(0xFF7C3AED),
                        bgColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFEDE9FE),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openFeedback() }
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardMetricMiniCard(
    title: String,
    value: String,
    unit: String,
    sub: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    waveform: List<Float>,
    waveColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(15.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 9.5.sp,
                maxLines = 1
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 8.5.sp
                )
            }

            // Real-time Sparkline Waveform Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(top = 2.dp)
            ) {
                SparklineWaveform(
                    dataPoints = waveform,
                    lineColor = waveColor,
                    strokeWidth = 3f,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun QuickActionMiniCard(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(17.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                fontSize = 9.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 11.sp
            )
        }
    }
}
