package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.ColorSkinTemp
import com.example.ui.theme.LightPrimary
import com.example.ui.theme.StatusOnlineGreen
import com.example.ui.theme.StressHighColor
import com.example.viewmodel.ActiveOverlay
import com.example.viewmodel.StressCareViewModel

@Composable
fun ProfileScreen(
    viewModel: StressCareViewModel,
    modifier: Modifier = Modifier
) {
    val userPrefs by viewModel.userPreferences.collectAsState()
    val connectedDevice by viewModel.bleManager.connectedDevice.collectAsState()
    val liveVitals by viewModel.bleManager.liveVitals.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val latestPss by viewModel.repository.latestPssAssessment.collectAsState(initial = null)
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var vibrateAlertsEnabled by remember { mutableStateOf(true) }
    var migraineWarningsEnabled by remember { mutableStateOf(true) }

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0B0F19)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFDEEEFF), Color(0xFFF0F6FF), Color(0xFFF5F8FF)))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("profile_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Profile Header Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(20.dp))
                        .testTag("profile_user_header"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2563EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AM",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = userPrefs?.name ?: "Alex Morgan",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFEF3C7))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "PRO",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFD97706),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Text(
                                text = userPrefs?.email ?: "alex.morgan@stresscare.ai",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = userPrefs?.phone ?: "+1 (555) 234-5678",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            // 2. Active Subscription Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF1E1B4B) else Color(0xFFFFFFFF)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDarkMode) Color(0xFF4338CA) else Color(0xFFC7D2FE)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
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
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = "Premium",
                                    tint = Color(0xFF4F46E5),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "STRESSCARE PRO PLAN",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF4F46E5),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF16A34A),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "Full Physiological Telemetry Suite Unlocked",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "• 24/7 ESP32-S3 BLE Continuous Sensor Telemetry\n• Real-Time Multi-Modal AI Stress Inference\n• Predictive Migraine Risk Forecasting\n• Offline-First SQLite Local Storage & Cloud Sync",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // 3. Theme & Appearance
            item {
                Text(
                    text = "APPEARANCE & DISPLAY",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isDarkMode) Color(0xFF1E293B) else Color(0xFFDBEAFE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = "Theme",
                                    tint = if (isDarkMode) Color(0xFFFBBF24) else Color(0xFF2563EB),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Dark Mode",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isDarkMode) "Enabled • Dark theme active" else "Disabled • Light theme (stresscarepp)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }

            // 4. Wearable Hardware Section
            item {
                Text(
                    text = "WEARABLE HARDWARE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
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
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFDBEAFE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bluetooth,
                                        contentDescription = "Device",
                                        tint = Color(0xFF2563EB),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = connectedDevice?.name ?: "StressCare Band 2.0",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "ID: ${connectedDevice?.id ?: "SC-ESP32-84F2"} • Firmware ${connectedDevice?.firmware ?: "v2.4.1"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "CONNECTED",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF16A34A),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.setOverlay(ActiveOverlay.BLE_PAIRING) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Pair / Scan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.setOverlay(ActiveOverlay.CALIBRATION) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2563EB).copy(alpha = 0.4f))
                            ) {
                                Text("Calibrate Baseline", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                            }
                        }
                    }
                }
            }

            // 5. Clinical Scale (PSS-10)
            item {
                Text(
                    text = "CLINICAL ASSESSMENT",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.openFeedback() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFEDE9FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Assignment,
                                    contentDescription = "PSS",
                                    tint = Color(0xFF7C3AED),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "Perceived Stress Scale (PSS-10)",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (latestPss != null) "Latest: ${latestPss?.score}/40 (${latestPss?.category})" else "Tap to log current feeling & scale",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF2563EB)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Take PSS",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // 6. Support & Documentation
            item {
                Text(
                    text = "SUPPORT & INFO",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        SupportMenuRow(
                            icon = Icons.Default.Info,
                            title = "View App Onboarding Tour",
                            subtitle = "Review system architecture & wearable guide",
                            onClick = { viewModel.setOverlay(ActiveOverlay.ONBOARDING) }
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )

                        SupportMenuRow(
                            icon = Icons.Default.HelpOutline,
                            title = "Hardware Sensor Technical Specs",
                            subtitle = "MAX30102 PPG • Grove GSR • MLX90614 IR • MPU6050",
                            onClick = { }
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )

                        SupportMenuRow(
                            icon = Icons.Default.Security,
                            title = "Privacy & Local Data Encryption",
                            subtitle = "All physiological logs stored offline in SQLite",
                            onClick = { }
                        )
                    }
                }
            }

            // 7. Logout Button
            item {
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("profile_logout_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StressHighColor.copy(alpha = 0.12f),
                        contentColor = StressHighColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Log Out",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign Out Account",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text("Confirm Sign Out", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to sign out? Your offline sensor recordings are safely stored in your local SQLite database.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.setOverlay(ActiveOverlay.AUTH)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StressHighColor)
                ) {
                    Text("Sign Out", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun SupportMenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Go",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(18.dp)
        )
    }
}
