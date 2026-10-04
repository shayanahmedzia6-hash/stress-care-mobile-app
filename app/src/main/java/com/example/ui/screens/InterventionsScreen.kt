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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PRESET_AUDIO_TRACKS
import com.example.ui.theme.StressHighColor
import com.example.viewmodel.ActiveOverlay
import com.example.viewmodel.StressCareViewModel

@Composable
fun InterventionsScreen(
    viewModel: StressCareViewModel,
    modifier: Modifier = Modifier
) {
    val liveVitals by viewModel.bleManager.liveVitals.collectAsState()
    val isPlaying by viewModel.audioEngine.isPlaying.collectAsState()
    val currentTrack by viewModel.audioEngine.currentTrack.collectAsState()
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
            .testTag("interventions_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Section Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Stress Interventions",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    )
                    Text(
                        text = "Clinically validated exercises for autonomic down-regulation",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 1. Alert Banner (if Stress is elevated or for quick emergency calm)
            item {
                val isElevated = liveVitals.stressScore >= 50
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(16.dp))
                        .testTag("interventions_alert_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isElevated) Color(0xFFFEF2F2) else Color(0xFFEEF2FF)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isElevated) Color(0xFFFECACA) else Color(0xFFC7D2FE)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isElevated) Color(0xFFFEE2E2) else Color(0xFFDBEAFE)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isElevated) Icons.Default.Warning else Icons.Default.Spa,
                                    contentDescription = "Intervention State",
                                    tint = if (isElevated) StressHighColor else Color(0xFF2563EB),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = if (isElevated) "Moderate Sympathetic Arousal" else "Autonomic State Balanced",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isElevated) Color(0xFF991B1B) else Color(0xFF1E3A8A),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isElevated) "Recommended: 2-min box breathing reset" else "Maintain steady focus with ambient audio",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isElevated) Color(0xFFB91C1C) else Color(0xFF3B82F6),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.setOverlay(ActiveOverlay.GUIDED_BREATHING) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isElevated) StressHighColor else Color(0xFF2563EB),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("interventions_calm_now_button")
                        ) {
                            Text(
                                text = "Calm Now",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // 2. Highlighted Recommendation Card (Box Breathing 95% Match)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(18.dp))
                        .clickable { viewModel.setOverlay(ActiveOverlay.GUIDED_BREATHING) }
                        .testTag("interventions_featured_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2563EB).copy(alpha = 0.3f))
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
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFDBEAFE))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "95% CLINICAL MATCH",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF2563EB),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "2 mins • 4-4-4-4",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDBEAFE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Air,
                                    contentDescription = "Breathing",
                                    tint = Color(0xFF2563EB),
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "Guided Box Breathing",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Vagus nerve activation via equal duration rhythmic breath retention",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.setOverlay(ActiveOverlay.GUIDED_BREATHING) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("interventions_start_breathing_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Start",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Begin Breathing Exercise",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 3. Audio Therapy & Synthesizer Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(18.dp))
                        .testTag("interventions_audio_player_card"),
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
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                                        imageVector = Icons.Default.Headphones,
                                        contentDescription = "Audio Therapy",
                                        tint = Color(0xFF7C3AED),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Binaural & Ambient Soundscapes",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isPlaying) "Playing: ${currentTrack.title}" else "Tap to listen • Alpha 10Hz Beats",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isPlaying) Color(0xFF16A34A) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.toggleAudioPlay() },
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF7C3AED))
                                    .testTag("interventions_audio_play_toggle")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Track Quick-Pills
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PRESET_AUDIO_TRACKS.take(3).forEach { track ->
                                val isSelected = currentTrack.id == track.id
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) Color(0xFF7C3AED).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) Color(0xFF7C3AED) else MaterialTheme.colorScheme.outlineVariant
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.selectAudioTrack(track) }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = track.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color(0xFF7C3AED) else MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = track.carrierNote,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Clinical Exercise Library Section
            item {
                Text(
                    text = "AUTONOMIC EXERCISES",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    InterventionItemRow(
                        title = "4-7-8 Parasympathetic Sedation",
                        duration = "3 mins",
                        category = "High Stress Relief",
                        description = "Extended exhalations maximize vagal brake engagement and lower pulse.",
                        accentColor = Color(0xFF2563EB),
                        icon = Icons.Default.Air,
                        onClick = { viewModel.setOverlay(ActiveOverlay.GUIDED_BREATHING) }
                    )

                    InterventionItemRow(
                        title = "Progressive Muscle Relaxation",
                        duration = "5 mins",
                        category = "Somatic Reset",
                        description = "Systematic tension and release cycles to dissipate somatic stress.",
                        accentColor = Color(0xFF7C3AED),
                        icon = Icons.Default.SelfImprovement,
                        onClick = { }
                    )

                    InterventionItemRow(
                        title = "Biofeedback Calibration Breath",
                        duration = "1 min",
                        category = "Baseline Check",
                        description = "Synchronize autonomic vitals with the wearable hardware before work.",
                        accentColor = Color(0xFF16A34A),
                        icon = Icons.Default.Spa,
                        onClick = { viewModel.setOverlay(ActiveOverlay.CALIBRATION) }
                    )
                }
            }
        }
    }
}

@Composable
fun InterventionItemRow(
    title: String,
    duration: String,
    category: String,
    description: String,
    accentColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
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
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = duration,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
