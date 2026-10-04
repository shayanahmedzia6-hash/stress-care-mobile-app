package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.StressSessionEntity
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.ColorHeartRate
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
import com.example.viewmodel.StressCareViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AnalyticsScreen(
    viewModel: StressCareViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) } // 0: Stress History, 1: Sleep & Migraine
    val timeRange by viewModel.analyticsTimeRange.collectAsState()
    val allSessions by viewModel.allSessions.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header & Segmented Tab
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "ANALYTICS & INSIGHTS",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                // Sub-tab: Stress History vs Sleep & Migraine
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp)),
                    color = DarkSurface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedSection == 0) CyanPrimary.copy(alpha = 0.2f) else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (selectedSection == 0) CyanPrimary else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedSection = 0 }
                                .padding(vertical = 10.dp)
                                .testTag("tab_stress_history"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Stress History",
                                style = MaterialTheme.typography.titleSmall,
                                color = if (selectedSection == 0) CyanPrimary else TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedSection == 1) PurpleAccent.copy(alpha = 0.2f) else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (selectedSection == 1) PurpleAccent else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedSection = 1 }
                                .padding(vertical = 10.dp)
                                .testTag("tab_sleep_migraine"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sleep & Migraine",
                                style = MaterialTheme.typography.titleSmall,
                                color = if (selectedSection == 1) PurpleAccent else TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Time Range Pills (Day, Week, Month)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Day", "Week", "Month").forEach { range ->
                        val isSelected = timeRange == range
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) DarkSurfaceHighlight else DarkSurface)
                                .border(1.dp, if (isSelected) CyanPrimary else DarkBorder, RoundedCornerShape(20.dp))
                                .clickable { viewModel.setAnalyticsTimeRange(range) }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = range,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) CyanPrimary else TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        if (selectedSection == 0) {
            // ================= STRESS HISTORY VIEW =================

            // 1. 7-Day Stress Trend Line Chart
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                        .testTag("analytics_trend_chart"),
                    color = DarkSurface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "7-DAY STRESS TREND",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Avg: 48%",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyanPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Canvas 7-Day Trend Line
                        val days = listOf("Mon" to 38, "Tue" to 58, "Wed" to 44, "Thu" to 72, "Fri" to 62, "Sat" to 32, "Sun" to 29)
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .padding(vertical = 8.dp)
                        ) {
                            val w = size.width
                            val h = size.height
                            val stepX = w / (days.size - 1)

                            // Benchmark threshold lines (Moderate 40%, High 70%)
                            val line40Y = h * (1f - 0.40f)
                            val line70Y = h * (1f - 0.70f)

                            drawLine(
                                color = DarkBorder,
                                start = Offset(0f, line40Y),
                                end = Offset(w, line40Y),
                                strokeWidth = 1f
                            )
                            drawLine(
                                color = DarkBorder,
                                start = Offset(0f, line70Y),
                                end = Offset(w, line70Y),
                                strokeWidth = 1f
                            )

                            val path = Path()
                            days.forEachIndexed { idx, pair ->
                                val x = idx * stepX
                                val y = h * (1f - (pair.second / 100f))
                                if (idx == 0) path.moveTo(x, y) else path.lineTo(x, y)
                            }

                            drawPath(
                                path = path,
                                color = CyanPrimary,
                                style = Stroke(width = 5f, cap = StrokeCap.Round)
                            )

                            // Draw nodes
                            days.forEachIndexed { idx, pair ->
                                val x = idx * stepX
                                val y = h * (1f - (pair.second / 100f))
                                val nodeColor = when {
                                    pair.second < 40 -> StressLowColor
                                    pair.second < 70 -> StressModerateColor
                                    else -> StressHighColor
                                }
                                drawCircle(color = DarkSurface, radius = 8f, center = Offset(x, y))
                                drawCircle(color = nodeColor, radius = 5f, center = Offset(x, y))
                            }
                        }

                        // Day Labels
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            days.forEach { pair ->
                                Text(
                                    text = pair.first,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // 2. Stress Distribution Donut Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                        .testTag("analytics_distribution_card"),
                    color = DarkSurface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "STRESS DISTRIBUTION",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            // Donut Canvas
                            Canvas(modifier = Modifier.size(110.dp)) {
                                val stroke = 18f
                                val diameter = size.minDimension - stroke
                                val topLeft = Offset(stroke / 2, stroke / 2)
                                val arcSize = Size(diameter, diameter)

                                // Low: 35% -> 126 deg
                                drawArc(
                                    color = StressLowColor,
                                    startAngle = -90f,
                                    sweepAngle = 126f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = arcSize,
                                    style = Stroke(stroke, cap = StrokeCap.Round)
                                )

                                // Moderate: 50% -> 180 deg
                                drawArc(
                                    color = StressModerateColor,
                                    startAngle = 36f,
                                    sweepAngle = 180f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = arcSize,
                                    style = Stroke(stroke, cap = StrokeCap.Round)
                                )

                                // High: 15% -> 54 deg
                                drawArc(
                                    color = StressHighColor,
                                    startAngle = 216f,
                                    sweepAngle = 54f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = arcSize,
                                    style = Stroke(stroke, cap = StrokeCap.Round)
                                )
                            }

                            // Distribution Legend & Breakdown
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DistributionRow(
                                    label = "Low Stress",
                                    percent = "35%",
                                    hours = "3.5 hrs/day",
                                    color = StressLowColor
                                )
                                DistributionRow(
                                    label = "Moderate Stress",
                                    percent = "50%",
                                    hours = "5.0 hrs/day",
                                    color = StressModerateColor
                                )
                                DistributionRow(
                                    label = "High Stress",
                                    percent = "15%",
                                    hours = "1.5 hrs/day",
                                    color = StressHighColor
                                )
                            }
                        }
                    }
                }
            }

            // 3. Historical Summary Metrics (2x2)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryMetricBox(
                        title = "Avg Stress",
                        value = "48%",
                        subtitle = "-6% vs last week",
                        accentColor = CyanPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryMetricBox(
                        title = "Avg Heart Rate",
                        value = "74 BPM",
                        subtitle = "Resting: 68 BPM",
                        accentColor = ColorHeartRate,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryMetricBox(
                        title = "Peak Window",
                        value = "2:00 - 3:30 PM",
                        subtitle = "Sprint review meeting",
                        accentColor = AmberWarning,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryMetricBox(
                        title = "Baseline Drift",
                        value = "+0.2 µS",
                        subtitle = "Tonic GSR stable",
                        accentColor = EmeraldAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 4. Recent Monitoring Sessions List
            item {
                Text(
                    text = "RECORDED SESSIONS",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            items(allSessions) { session ->
                SessionCardItem(session)
            }

        } else {
            // ================= SLEEP & MIGRAINE INSIGHTS VIEW =================

            // 1. Sleep Quality Score Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, PurpleAccent.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        .testTag("insights_sleep_card"),
                    color = DarkSurface
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
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
                                    imageVector = Icons.Default.Bedtime,
                                    contentDescription = "Sleep",
                                    tint = PurpleAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "LAST NIGHT SLEEP QUALITY",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PurpleAccent,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }

                            Text(
                                text = "7h 24m Total",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "76",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = "/100",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                }
                                Text(
                                    text = "Restful • Optimal REM Cycles",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EmeraldAccent
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Overnight HRV: 52 ms",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Resting HR: 59 BPM",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Sleep Stage Stacked Bar
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(0.26f)
                                        .fillMaxSize()
                                        .background(Color(0xFF6366F1)) // Deep
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(0.24f)
                                        .fillMaxSize()
                                        .background(PurpleAccent) // REM
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(0.50f)
                                        .fillMaxSize()
                                        .background(Color(0xFF38BDF8)) // Light
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Deep: 1h 55m (26%)", color = Color(0xFF6366F1), fontSize = 10.sp)
                                Text("REM: 1h 45m (24%)", color = PurpleAccent, fontSize = 10.sp)
                                Text("Light: 3h 44m (50%)", color = Color(0xFF38BDF8), fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            // 2. Migraine Risk Level Gauge
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, AmberWarning.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .testTag("insights_migraine_gauge"),
                    color = DarkSurface
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MIGRAINE RISK FORECAST",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(AmberWarning.copy(alpha = 0.2f))
                                    .border(1.dp, AmberWarning, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "MEDIUM RISK",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AmberWarning,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(80.dp)
                            ) {
                                CircularProgressIndicator(
                                    progress = { 1f },
                                    modifier = Modifier.size(80.dp),
                                    color = DarkBorder,
                                    strokeWidth = 8.dp
                                )
                                CircularProgressIndicator(
                                    progress = { 0.58f },
                                    modifier = Modifier.size(80.dp),
                                    color = AmberWarning,
                                    strokeWidth = 8.dp,
                                    strokeCap = StrokeCap.Round
                                )
                                Text(
                                    text = "58%",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Elevated Autonomic Stress Indicator",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Multi-modal model detected sympathetic tone surge combined with sleep fragmentation.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // 3. Detected Risk Factors List
            item {
                Text(
                    text = "PHYSIOLOGICAL RISK FACTORS DETECTED",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    RiskFactorItem(
                        title = "HRV Sudden Nocturnal Drop",
                        description = "RMSSD decreased by -18% compared to your 30-day baseline.",
                        severityColor = AmberWarning
                    )
                    RiskFactorItem(
                        title = "Fragmented Sleep Continuity",
                        description = "3 micro-awakenings identified via MPU6050 accelerometer sensors.",
                        severityColor = AmberWarning
                    )
                    RiskFactorItem(
                        title = "Sustained Sympathetic GSR Spikes",
                        description = "4 sustained electrodermal arousal events during yesterday afternoon.",
                        severityColor = RosePulse
                    )
                }
            }

            // 4. Clinical Recommendations
            item {
                Text(
                    text = "PREVENTIVE CLINICAL RECOMMENDATIONS",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    RecommendationItem(
                        title = "Hydrate: Drink 500ml Water",
                        description = "Reduces cranial microvascular constriction and autonomic irritability."
                    )
                    RecommendationItem(
                        title = "Pre-Sleep Digital Wind-down",
                        description = "Avoid blue-spectrum displays 45 minutes prior to sleep to elevate melatonin."
                    )
                    RecommendationItem(
                        title = "Perform 2-Min Resonant Box Breathing",
                        description = "Restores vagal parasympathetic cardiac inhibition via baroreceptor activation."
                    )
                }
            }
        }
    }
}

@Composable
fun DistributionRow(
    label: String,
    percent: String,
    hours: String,
    color: Color
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
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextPrimary
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = percent,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = hours,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun SummaryMetricBox(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp)),
        color = DarkSurface
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = accentColor,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun SessionCardItem(session: StressSessionEntity) {
    val dateStr = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(session.startTime))
    val badgeColor = when {
        session.avgStress < 40 -> StressLowColor
        session.avgStress < 70 -> StressModerateColor
        else -> StressHighColor
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp)),
        color = DarkSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = session.label,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$dateStr • ${(session.durationSeconds / 60)} mins",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeColor.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${session.avgStress}% Stress",
                        style = MaterialTheme.typography.labelSmall,
                        color = badgeColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Avg HR: ${session.avgHeartRate} bpm",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun RiskFactorItem(
    title: String,
    description: String,
    severityColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp)),
        color = DarkSurface
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(severityColor)
                    .padding(top = 4.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun RecommendationItem(
    title: String,
    description: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp)),
        color = DarkSurface
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Recommendation",
                tint = EmeraldAccent,
                modifier = Modifier.size(18.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}
