package com.example.ui.overlays

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RosePulse
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.BreathingPhase
import com.example.viewmodel.StressCareViewModel

@Composable
fun GuidedBreathingOverlay(
    viewModel: StressCareViewModel,
    onDismiss: () -> Unit
) {
    val phase by viewModel.breathingPhase.collectAsState()
    val progress by viewModel.breathingPhaseProgress.collectAsState()
    val totalSecsRemaining by viewModel.breathingTotalRemainingSeconds.collectAsState()
    val cycleCount by viewModel.breathingCycleCount.collectAsState()
    val liveVitals by viewModel.bleManager.liveVitals.collectAsState()

    val formattedRemaining = String.format("%02d:%02d", totalSecsRemaining / 60, totalSecsRemaining % 60)

    val targetSize = when (phase) {
        BreathingPhase.INHALE -> 230.dp
        BreathingPhase.HOLD_IN -> 230.dp
        BreathingPhase.EXHALE -> 130.dp
        BreathingPhase.HOLD_OUT -> 130.dp
    }

    val animatedSize by animateDpAsState(
        targetValue = targetSize,
        animationSpec = tween(durationMillis = 3800, easing = FastOutSlowInEasing),
        label = "circle_size"
    )

    val themeColor = when (phase) {
        BreathingPhase.INHALE -> CyanPrimary
        BreathingPhase.HOLD_IN -> PurpleAccent
        BreathingPhase.EXHALE -> EmeraldAccent
        BreathingPhase.HOLD_OUT -> Color(0xFF38BDF8)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("guided_breathing_overlay"),
        color = DarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyanPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Air,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "BOX BREATHING (4-4-4-4)",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cycle $cycleCount • $formattedRemaining remaining",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Exit", tint = TextSecondary)
                }
            }

            // Live HR Monitor Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = DarkBorder.copy(alpha = 0.5f),
                modifier = Modifier.border(1.dp, RosePulse.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Pulse",
                        tint = RosePulse,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Pulse: ${liveVitals.heartRate} BPM (Stabilizing)",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextPrimary
                    )
                }
            }

            // Central Animated Breathing Sphere
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(260.dp)
            ) {
                // Outer subtle aura
                Box(
                    modifier = Modifier
                        .size(animatedSize + 30.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(themeColor.copy(alpha = 0.25f), Color.Transparent)
                            )
                        )
                )

                // Main expanding sphere
                Box(
                    modifier = Modifier
                        .size(animatedSize)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(themeColor.copy(alpha = 0.85f), themeColor.copy(alpha = 0.4f))
                            )
                        )
                        .border(3.dp, themeColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = when (phase) {
                                BreathingPhase.INHALE -> "INHALE"
                                BreathingPhase.HOLD_IN -> "HOLD"
                                BreathingPhase.EXHALE -> "EXHALE"
                                BreathingPhase.HOLD_OUT -> "REST"
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "4 Seconds",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Phase Instruction Text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = phase.label,
                    style = MaterialTheme.typography.titleMedium,
                    color = themeColor,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Follow the expanding circle. Gentle haptics guide each phase transition.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            // Complete Session Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("guided_breathing_complete_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanPrimary,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "End Exercise",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
