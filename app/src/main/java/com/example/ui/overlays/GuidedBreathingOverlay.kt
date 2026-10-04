package com.example.ui.overlays

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ColorHeartRate
import com.example.ui.theme.StatusOnlineGreen
import com.example.viewmodel.BreathingPhase
import com.example.viewmodel.StressCareViewModel

@Composable
fun GuidedBreathingOverlay(
    viewModel: StressCareViewModel,
    onDismiss: () -> Unit
) {
    val phase by viewModel.breathingPhase.collectAsState()
    val secondsInPhase by viewModel.secondsInPhase.collectAsState()
    val totalSecondsRemaining by viewModel.breathingTotalSecondsRemaining.collectAsState()
    val cycleCount by viewModel.breathingCycleCount.collectAsState()
    val liveVitals by viewModel.bleManager.liveVitals.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val targetSphereScale = when (phase) {
        BreathingPhase.INHALE -> 1.0f
        BreathingPhase.HOLD_IN -> 1.0f
        BreathingPhase.EXHALE -> 0.45f
        BreathingPhase.HOLD_OUT -> 0.45f
    }

    val animatedSphereScale by animateFloatAsState(
        targetValue = targetSphereScale,
        animationSpec = tween(durationMillis = 3800, easing = FastOutSlowInEasing),
        label = "breathingSphereScale"
    )

    val currentPhaseColor = when (phase) {
        BreathingPhase.INHALE -> Color(0xFF2563EB)
        BreathingPhase.HOLD_IN -> Color(0xFF7C3AED)
        BreathingPhase.EXHALE -> StatusOnlineGreen
        BreathingPhase.HOLD_OUT -> Color(0xFF0D9488)
    }

    val mins = totalSecondsRemaining / 60
    val secs = totalSecondsRemaining % 60
    val formattedRemaining = String.format("%02d:%02d", mins, secs)

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0B0F19)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFEAF3FF), Color(0xFFF0F6FF), Color(0xFFF5F8FF)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("guided_breathing_overlay")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
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
                            imageVector = Icons.Default.Air,
                            contentDescription = null,
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Box Breathing (4-4-4-4)",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cycle $cycleCount • $formattedRemaining remaining",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Exit", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Live HR Monitor Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier.border(1.dp, ColorHeartRate.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Pulse",
                        tint = ColorHeartRate,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Pulse: ${liveVitals.heartRate} BPM (Stabilizing)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Central Animated Breathing Sphere
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                // Outer subtle aura
                Box(
                    modifier = Modifier
                        .size((240.dp * animatedSphereScale).coerceAtLeast(100.dp))
                        .clip(CircleShape)
                        .background(currentPhaseColor.copy(alpha = 0.12f))
                )

                // Middle pulsing ring
                Box(
                    modifier = Modifier
                        .size((200.dp * animatedSphereScale).coerceAtLeast(80.dp))
                        .clip(CircleShape)
                        .background(currentPhaseColor.copy(alpha = 0.22f))
                        .border(2.dp, currentPhaseColor.copy(alpha = 0.6f), CircleShape)
                )

                // Core Sphere
                Box(
                    modifier = Modifier
                        .size((150.dp * animatedSphereScale).coerceAtLeast(60.dp))
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    currentPhaseColor.copy(alpha = 0.9f),
                                    currentPhaseColor.copy(alpha = 0.6f)
                                )
                            )
                        )
                        .shadow(12.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$secondsInPhase",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Phase Instruction Label
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = phase.name.replace("_", " "),
                    style = MaterialTheme.typography.titleLarge,
                    color = currentPhaseColor,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = phase.label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Autonomic tone balances as exhalations slow heart rhythm",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Stop / Finish Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("guided_breathing_stop_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Text("End Breathing Exercise", fontWeight = FontWeight.Bold)
            }
        }
    }
}
