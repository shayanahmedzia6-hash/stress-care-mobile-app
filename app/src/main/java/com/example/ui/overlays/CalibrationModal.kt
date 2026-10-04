package com.example.ui.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StatusOnlineGreen
import com.example.viewmodel.CalibrationStep
import com.example.viewmodel.StressCareViewModel

@Composable
fun CalibrationModal(
    viewModel: StressCareViewModel,
    onDismiss: () -> Unit
) {
    val secondsRemaining by viewModel.calibrationSecondsRemaining.collectAsState()
    val progress by viewModel.calibrationProgress.collectAsState()
    val steps by viewModel.calibrationSteps.collectAsState()
    val isDone = secondsRemaining <= 0
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
            .testTag("calibration_modal")
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
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Tune",
                            tint = Color(0xFF7C3AED),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Baseline Calibration",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Wearable Autonomic Profiling",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Circular Countdown Timer
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(180.dp)
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(180.dp),
                    color = if (isDarkMode) Color(0xFF334155) else Color(0xFFE2E8F0),
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(180.dp),
                    color = if (isDone) StatusOnlineGreen else Color(0xFF7C3AED),
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Complete",
                            tint = StatusOnlineGreen,
                            modifier = Modifier.size(44.dp)
                        )
                        Text(
                            text = "CALIBRATED",
                            style = MaterialTheme.typography.labelSmall,
                            color = StatusOnlineGreen,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "${secondsRemaining}s",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "REST STILL",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Text(
                text = if (isDone)
                    "Personal autonomic baseline established! Sensor inferences are now calibrated."
                else
                    "Remain seated and breathe naturally while sensors calibrate your resting GSR, heart rate, and skin temp.",
                style = MaterialTheme.typography.bodySmall,
                color = if (isDone) StatusOnlineGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Step Progress Checklist
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(steps) { step ->
                    CalibrationStepRow(step = step, isDarkMode = isDarkMode)
                }
            }

            // Action Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("calibration_done_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDone) StatusOnlineGreen else MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (isDone) "Finish & Apply" else "Cancel Calibration",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun CalibrationStepRow(step: CalibrationStep, isDarkMode: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (step.isCompleted) androidx.compose.foundation.BorderStroke(1.dp, StatusOnlineGreen.copy(alpha = 0.4f)) else null
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            step.isCompleted -> Color(0xFFDCFCE7)
                            step.isInProgress -> Color(0xFFEDE9FE)
                            else -> if (isDarkMode) Color(0xFF1E293B) else Color(0xFFF1F5F9)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (step.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        tint = StatusOnlineGreen,
                        modifier = Modifier.size(16.dp)
                    )
                } else if (step.isInProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        color = Color(0xFF7C3AED),
                        strokeWidth = 2.dp
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = step.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }

            if (step.value.isNotEmpty()) {
                Text(
                    text = step.value,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (step.isCompleted) StatusOnlineGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
