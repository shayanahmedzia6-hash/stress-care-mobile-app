package com.example.ui.overlays

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
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ble.DiscoveredDevice
import com.example.ui.theme.StatusOnlineGreen
import com.example.ui.theme.StressHighColor
import com.example.viewmodel.StressCareViewModel

@Composable
fun BlePairingModal(
    viewModel: StressCareViewModel,
    onDismiss: () -> Unit
) {
    val isScanning by viewModel.bleManager.isScanning.collectAsState()
    val discoveredDevices by viewModel.bleManager.discoveredDevices.collectAsState()
    val connectedDevice by viewModel.bleManager.connectedDevice.collectAsState()
    val isConnected by viewModel.bleManager.isConnected.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "ble_radar")
    val radarPulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radarPulse"
    )

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0B0F19)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFEAF3FF), Color(0xFFF0F6FF), Color(0xFFF5F8FF)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("ble_pairing_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFDBEAFE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bluetooth,
                            contentDescription = "Bluetooth",
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Pair Wearable Band",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ESP32-S3 BLE GATT Profile",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Radar Visualizer & Scan Toggle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isScanning) {
                    Box(
                        modifier = Modifier
                            .size(120.dp * radarPulse)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB).copy(alpha = 0.12f))
                    )
                    Box(
                        modifier = Modifier
                            .size(90.dp * radarPulse)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB).copy(alpha = 0.18f))
                    )
                }

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(2.dp, Color(0xFF2563EB), CircleShape)
                        .clickable {
                            if (isScanning) viewModel.bleManager.stopScan()
                            else viewModel.bleManager.startScan()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isScanning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            color = Color(0xFF2563EB),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Scan",
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Text(
                text = if (isScanning) "Searching for nearby StressCare Smart Bands..." else "Tap radar icon above to rescan",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Discovered Devices List
            Text(
                text = "DISCOVERED DEVICES (${discoveredDevices.size})",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (discoveredDevices.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "No devices found yet",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Ensure your ESP32-S3 band is powered on and within Bluetooth range.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(discoveredDevices) { device ->
                        DeviceRowItem(
                            device = device,
                            onConnect = { viewModel.bleManager.connectToDevice(device) },
                            onDisconnect = { viewModel.bleManager.disconnect() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Done Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("ble_pairing_done_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Done", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun DeviceRowItem(
    device: DiscoveredDevice,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit
) {
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (device.isConnected) Color(0xFFDCFCE7) else Color(0xFFDBEAFE)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bluetooth,
                        contentDescription = device.name,
                        tint = if (device.isConnected) StatusOnlineGreen else Color(0xFF2563EB),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = device.name,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (device.isConnected) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "CONNECTED",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = StatusOnlineGreen,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Text(
                        text = "RSSI: ${device.rssi} dBm • Firmware ${device.firmware} • ${device.batteryLevel}% Bat",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }

            if (device.isConnected) {
                Button(
                    onClick = onDisconnect,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StressHighColor.copy(alpha = 0.15f),
                        contentColor = StressHighColor
                    )
                ) {
                    Text("Disconnect", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onConnect,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Connect", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
