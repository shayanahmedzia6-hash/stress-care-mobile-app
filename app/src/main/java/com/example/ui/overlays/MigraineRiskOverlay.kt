package com.example.ui.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MigraineBorderLight
import com.example.ui.theme.MigraineCardBgEnd
import com.example.ui.theme.MigraineCardBgStart
import com.example.ui.theme.MigraineTextLight
import com.example.ui.theme.StatusOnlineGreen
import com.example.viewmodel.ActiveOverlay
import com.example.viewmodel.StressCareViewModel

@Composable
fun MigraineRiskOverlay(
    viewModel: StressCareViewModel,
    onDismiss: () -> Unit
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0B0F19)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFEAF3FF), Color(0xFFF0F6FF), Color(0xFFF5F8FF)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("migraine_risk_overlay")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column {
                            Text(
                                text = "Migraine Risk",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "AI Early Warning & Physiological Factors",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Summary Card (Light theme mint gradient from stresscarepp.netlify.app)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
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
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(if (isDarkMode) Color(0xFF059669) else Color(0xFFD1FAE5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = if (isDarkMode) Color.White else MigraineTextLight,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Current Risk Status",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isDarkMode) Color(0xFFA7F3D0) else MigraineTextLight,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Medium Risk (58/100)",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isDarkMode) Color.White else MigraineTextLight,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "Forecasted window: Next 6 - 8 hours",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isDarkMode) Color(0xFFA7F3D0) else Color(0xFF059669)
                                )
                            }
                        }
                    }
                }
            }

            // Risk Factors Section
            item {
                Text(
                    text = "Identified Risk Factors",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

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
                        RiskFactorRow(
                            icon = Icons.Default.Bedtime,
                            iconColor = Color(0xFF8B5CF6),
                            title = "Nocturnal HRV Drop",
                            description = "RMSSD dropped 24% below personal baseline during REM sleep.",
                            status = "Elevated Risk",
                            statusColor = Color(0xFFF59E0B)
                        )
                        RiskFactorRow(
                            icon = Icons.Default.Waves,
                            iconColor = Color(0xFF0D9488),
                            title = "Sympathetic GSR Spikes",
                            description = "Electrodermal phasic bursts detected during afternoon hours.",
                            status = "Active",
                            statusColor = Color(0xFFEF4444)
                        )
                        RiskFactorRow(
                            icon = Icons.Default.TrendingUp,
                            iconColor = Color(0xFF2563EB),
                            title = "Peripheral Vasoconstriction",
                            description = "Skin temperature dipped 1.1°C compared to core estimation.",
                            status = "Moderate",
                            statusColor = Color(0xFFF59E0B)
                        )
                    }
                }
            }

            // Recommendations Section
            item {
                Text(
                    text = "Clinical Recommendations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

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
                        RecommendationItem(
                            icon = Icons.Default.WaterDrop,
                            iconColor = Color(0xFF2563EB),
                            title = "Hydration & Electrolytes",
                            desc = "Drink 500ml water with magnesium or electrolytes within 30 minutes."
                        )
                        RecommendationItem(
                            icon = Icons.Default.Spa,
                            iconColor = Color(0xFF0D9488),
                            title = "Guided Vagal Down-Regulation",
                            desc = "Perform a 5-minute Box Breathing session to stimulate parasympathetic tone."
                        )
                        RecommendationItem(
                            icon = Icons.Default.Info,
                            iconColor = Color(0xFFF59E0B),
                            title = "Reduce Screen Glare & Lighting",
                            desc = "Dim device brightness or wear blue-light blocking glasses."
                        )
                    }
                }
            }

            // Action Button
            item {
                Button(
                    onClick = {
                        onDismiss()
                        viewModel.setOverlay(ActiveOverlay.GUIDED_BREATHING)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Start Vagal Breathing Now",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun RiskFactorRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    description: String,
    status: String,
    statusColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun RecommendationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}
