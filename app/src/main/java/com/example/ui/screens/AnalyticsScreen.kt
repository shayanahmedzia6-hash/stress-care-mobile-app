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
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.theme.StatusOnlineGreen
import com.example.ui.theme.StressHighColor
import com.example.ui.theme.StressLowColor
import com.example.ui.theme.StressModerateColor
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
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0B0F19)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFDEEEFF), Color(0xFFF0F6FF), Color(0xFFF5F8FF)))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("analytics_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Section Header & Segmented Tab
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "History & Insights",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    )

                    // Material 3 Segmented TabRow
                    TabRow(
                        selectedTabIndex = selectedSection,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedSection]),
                                color = MaterialTheme.colorScheme.primary,
                                height = 3.dp
                            )
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .shadow(2.dp, RoundedCornerShape(14.dp))
                    ) {
                        Tab(
                            selected = selectedSection == 0,
                            onClick = { selectedSection = 0 },
                            text = {
                                Text(
                                    text = "Stress History",
                                    fontWeight = if (selectedSection == 0) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Timeline,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )

                        Tab(
                            selected = selectedSection == 1,
                            onClick = { selectedSection = 1 },
                            text = {
                                Text(
                                    text = "Sleep & Migraine",
                                    fontWeight = if (selectedSection == 1) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Bedtime,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }
            }

            if (selectedSection == 0) {
                // Section 0: Stress History Tab
                item {
                    // Time Range Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Day", "Week", "Month").forEach { range ->
                            val isSelected = timeRange == range
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .clickable { viewModel.setAnalyticsTimeRange(range) }
                                    .shadow(1.dp, RoundedCornerShape(20.dp))
                            ) {
                                Text(
                                    text = range,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // 1. 7-Day Stress Trend Line Chart
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, RoundedCornerShape(16.dp))
                            .testTag("analytics_trend_chart"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                                Text(
                                    text = "7-DAY STRESS TREND",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Avg: 48%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF2563EB),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Canvas 7-Day Trend Line
                            val days = listOf("Mon" to 38, "Tue" to 58, "Wed" to 44, "Thu" to 72, "Fri" to 62, "Sat" to 32, "Sun" to 29)
                            val gridColor = if (isDarkMode) Color(0xFF334155) else Color(0xFFE2E8F0)
                            val surfaceColor = MaterialTheme.colorScheme.surface

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .padding(vertical = 8.dp)
                            ) {
                                val w = size.width
                                val h = size.height
                                val stepX = w / (days.size - 1)

                                val line40Y = h * (1f - 0.40f)
                                val line70Y = h * (1f - 0.70f)

                                drawLine(color = gridColor, start = Offset(0f, line40Y), end = Offset(w, line40Y), strokeWidth = 1f)
                                drawLine(color = gridColor, start = Offset(0f, line70Y), end = Offset(w, line70Y), strokeWidth = 1f)

                                val path = Path()
                                days.forEachIndexed { idx, pair ->
                                    val x = idx * stepX
                                    val y = h * (1f - (pair.second / 100f))
                                    if (idx == 0) path.moveTo(x, y) else path.lineTo(x, y)
                                }

                                drawPath(
                                    path = path,
                                    color = Color(0xFF2563EB),
                                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                                )

                                days.forEachIndexed { idx, pair ->
                                    val x = idx * stepX
                                    val y = h * (1f - (pair.second / 100f))
                                    val nodeColor = when {
                                        pair.second < 40 -> StressLowColor
                                        pair.second < 70 -> StressModerateColor
                                        else -> StressHighColor
                                    }
                                    drawCircle(color = surfaceColor, radius = 7f, center = Offset(x, y))
                                    drawCircle(color = nodeColor, radius = 4.5f, center = Offset(x, y))
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                days.forEach { pair ->
                                    Text(
                                        text = pair.first,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Stress Distribution Donut Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, RoundedCornerShape(16.dp))
                            .testTag("analytics_distribution_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "STRESS DISTRIBUTION (LAST 7 DAYS)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                // Donut Canvas
                                Canvas(modifier = Modifier.size(100.dp)) {
                                    val stroke = 16f
                                    val diameter = size.minDimension - stroke
                                    val topLeft = Offset(stroke / 2, stroke / 2)
                                    val arcSize = Size(diameter, diameter)

                                    drawArc(color = StressLowColor, startAngle = -90f, sweepAngle = 126f, useCenter = false, topLeft = topLeft, size = arcSize, style = Stroke(width = stroke, cap = StrokeCap.Round))
                                    drawArc(color = StressModerateColor, startAngle = 36f, sweepAngle = 162f, useCenter = false, topLeft = topLeft, size = arcSize, style = Stroke(width = stroke, cap = StrokeCap.Round))
                                    drawArc(color = StressHighColor, startAngle = 198f, sweepAngle = 72f, useCenter = false, topLeft = topLeft, size = arcSize, style = Stroke(width = stroke, cap = StrokeCap.Round))
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    DistributionLegendRow(color = StressLowColor, label = "Low Stress", percent = "35%")
                                    DistributionLegendRow(color = StressModerateColor, label = "Moderate Stress", percent = "45%")
                                    DistributionLegendRow(color = StressHighColor, label = "High Stress", percent = "20%")
                                }
                            }
                        }
                    }
                }

                // 3. Summary Metric Stats
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SummaryMetricCard("Avg Heart Rate", "74 BPM", "Normal", ColorHeartRate, Modifier.weight(1f))
                        SummaryMetricCard("Overnight HRV", "58 ms", "+8% recovery", Color(0xFF2563EB), Modifier.weight(1f))
                    }
                }

                // 4. Recent Recorded Sessions
                item {
                    Text(
                        text = "RECENT SESSIONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                if (allSessions.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Text(
                                text = "Telemetry sessions are logged automatically while wearable is connected.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else {
                    items(allSessions.take(5)) { session ->
                        SessionCardItem(session)
                    }
                }

            } else {
                // Section 1: Sleep & Migraine Insights
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkMode) Color(0xFF1E1B4B) else Color(0xFFFFFFFF)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDarkMode) Color(0xFF4338CA) else Color(0xFFE2E8F0)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF6366F1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bedtime,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Last Night Sleep Quality",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF6366F1),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "7h 24m • 85% Restful",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Deep sleep duration: 1h 48m • Autonomic recovery good",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Migraine Early Warning Triggers
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "MIGRAINE RISK SIGNALS",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            RiskFactorItem("Nocturnal HRV Drop", "RMSSD dropped below personal baseline.", Color(0xFFF59E0B))
                            RiskFactorItem("Sympathetic GSR Spikes", "Elevated phasic skin conductance bursts.", Color(0xFFEF4444))
                            RiskFactorItem("Vasoconstriction Dip", "Skin temperature dipped 1.1°C.", Color(0xFF2563EB))
                        }
                    }
                }

                // Clinical Recommendations
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "RECOMMENDED INTERVENTIONS",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            RecommendationItem("Electrolyte Hydration", "Drink 500ml water with electrolytes.")
                            RecommendationItem("Guided Vagal Reset", "Perform 5-min Box Breathing.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DistributionLegendRow(color: Color, label: String, percent: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = percent,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SummaryMetricCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SessionCardItem(session: StressSessionEntity) {
    val dateStr = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(session.startTime))
    val badgeColor = when {
        session.avgStress < 40 -> StressLowColor
        session.avgStress < 70 -> StressModerateColor
        else -> StressHighColor
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = session.label,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$dateStr • ${(session.durationSeconds / 60)} mins",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun RiskFactorItem(
    title: String,
    description: String,
    severityColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecommendationItem(
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Recommendation",
            tint = StatusOnlineGreen,
            modifier = Modifier.size(18.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
