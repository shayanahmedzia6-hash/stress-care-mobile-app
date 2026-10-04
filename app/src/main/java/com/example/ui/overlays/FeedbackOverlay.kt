package com.example.ui.overlays

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PSS_OPTIONS
import com.example.model.PSS_QUESTIONS
import com.example.viewmodel.StressCareViewModel

@Composable
fun FeedbackOverlay(
    viewModel: StressCareViewModel,
    onDismiss: () -> Unit
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    var selectedMood by remember { mutableStateOf("Moderate") }
    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var selectedAnswers by remember { mutableStateOf(IntArray(10) { 1 }) }
    var userNotes by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0B0F19)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFEAF3FF), Color(0xFFF0F6FF), Color(0xFFF5F8FF)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("feedback_overlay")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Bar
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
                            text = "Feedback & PSS Assessment",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Log your real-time feel and weekly clinical scale",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (!submitted) {
                // Section 1: How do you feel right now?
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "How do you feel right now?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                FeelButton(
                                    label = "Low",
                                    icon = Icons.Default.Mood,
                                    selected = selectedMood == "Low",
                                    color = Color(0xFF16A34A),
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedMood = "Low" }
                                )
                                FeelButton(
                                    label = "Moderate",
                                    icon = Icons.Default.SentimentNeutral,
                                    selected = selectedMood == "Moderate",
                                    color = Color(0xFF2563EB),
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedMood = "Moderate" }
                                )
                                FeelButton(
                                    label = "High",
                                    icon = Icons.Default.MoodBad,
                                    selected = selectedMood == "High",
                                    color = Color(0xFFEF4444),
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedMood = "High" }
                                )
                            }
                        }
                    }
                }

                // Section 2: PSS Questionnaire Card
                item {
                    val currentQuestion = PSS_QUESTIONS[currentQuestionIdx]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                                    text = "Weekly PSS Questionnaire",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Q${currentQuestionIdx + 1}/10",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Text(
                                text = currentQuestion.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 20.sp
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                PSS_OPTIONS.forEach { opt ->
                                    val optLabel = opt.first
                                    val optVal = opt.second
                                    val isChosen = selectedAnswers[currentQuestionIdx] == optVal
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                if (isChosen) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                                else Color.Transparent
                                            )
                                            .clickable {
                                                val copy = selectedAnswers.clone()
                                                copy[currentQuestionIdx] = optVal
                                                selectedAnswers = copy
                                            }
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        RadioButton(
                                            selected = isChosen,
                                            onClick = {
                                                val copy = selectedAnswers.clone()
                                                copy[currentQuestionIdx] = optVal
                                                selectedAnswers = copy
                                            },
                                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                        )
                                        Text(
                                            text = optLabel,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }

                            // Prev & Next Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { if (currentQuestionIdx > 0) currentQuestionIdx-- },
                                    enabled = currentQuestionIdx > 0,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Previous")
                                }

                                Button(
                                    onClick = { if (currentQuestionIdx < 9) currentQuestionIdx++ },
                                    enabled = currentQuestionIdx < 9,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Next")
                                }
                            }
                        }
                    }
                }

                // Section 3: Notes
                item {
                    OutlinedTextField(
                        value = userNotes,
                        onValueChange = { userNotes = it },
                        label = { Text("Additional Notes / Symptoms (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        minLines = 2
                    )
                }

                // Submit Button
                item {
                    Button(
                        onClick = {
                            viewModel.submitPssAssessment()
                            submitted = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_feedback_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Submit Feedback",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                // Success View
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDCFCE7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Text(
                                text = "Feedback Submitted!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Your self-reported status has been correlated with sensor telemetry and saved to your baseline model.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Done", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun FeelButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) color else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() },
        color = if (selected) color.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) color else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
