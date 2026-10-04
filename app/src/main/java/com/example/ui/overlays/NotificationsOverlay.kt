package com.example.ui.overlays

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.StressCareViewModel

data class NotificationItem(
    val title: String,
    val description: String,
    val time: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBg: Color
)

@Composable
fun NotificationsOverlay(
    viewModel: StressCareViewModel,
    onDismiss: () -> Unit
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0B0F19)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFEAF3FF), Color(0xFFF0F6FF), Color(0xFFF5F8FF)))
    }

    val todayNotifications = listOf(
        NotificationItem(
            title = "High Stress Alert",
            description = "Sympathetic index reached 78%. Recommended 5-minute Box Breathing.",
            time = "Today, 2:45 PM",
            icon = Icons.Default.Warning,
            iconColor = Color(0xFFEF4444),
            iconBg = Color(0xFFFEE2E2)
        ),
        NotificationItem(
            title = "Migraine Risk Warning",
            description = "Nocturnal HRV dip and rising GSR indicate elevated migraine probability.",
            time = "Today, 1:25 PM",
            icon = Icons.Default.NotificationsActive,
            iconColor = Color(0xFFF59E0B),
            iconBg = Color(0xFFFEF3C7)
        ),
        NotificationItem(
            title = "Weekly Report Ready",
            description = "Your 7-day autonomic stress and sleep recovery report has been compiled.",
            time = "Today, 9:00 AM",
            icon = Icons.Default.Insights,
            iconColor = Color(0xFF2563EB),
            iconBg = Color(0xFFDBEAFE)
        ),
        NotificationItem(
            title = "Calibration Reminder",
            description = "Please perform a 60-second resting calibration before evening activities.",
            time = "Today, 8:30 AM",
            icon = Icons.Default.Tune,
            iconColor = Color(0xFF7C3AED),
            iconBg = Color(0xFFEDE9FE)
        )
    )

    val yesterdayNotifications = listOf(
        NotificationItem(
            title = "Device Connected",
            description = "StressCare Band 2.0 connected via BLE. Telemetry stream active at 50Hz.",
            time = "Yesterday, 7:45 PM",
            icon = Icons.Default.BluetoothConnected,
            iconColor = Color(0xFF16A34A),
            iconBg = Color(0xFFDCFCE7)
        ),
        NotificationItem(
            title = "Sleep Analysis Completed",
            description = "Recorded 7h 24m sleep with 1h 48m deep recovery stage. Sleep score: 85.",
            time = "Yesterday, 7:00 AM",
            icon = Icons.Default.Bedtime,
            iconColor = Color(0xFF6366F1),
            iconBg = Color(0xFFE0E7FF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("notifications_overlay")
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
                            text = "Notifications",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Real-time alerts and clinical reminders",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Today Section
            item {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            items(todayNotifications) { notif ->
                NotificationCard(notif, isDarkMode)
            }

            // Yesterday Section
            item {
                Text(
                    text = "Yesterday",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(yesterdayNotifications) { notif ->
                NotificationCard(notif, isDarkMode)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun NotificationCard(item: NotificationItem, isDarkMode: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDarkMode) item.iconColor.copy(alpha = 0.2f) else item.iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.iconColor,
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
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = item.time,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
