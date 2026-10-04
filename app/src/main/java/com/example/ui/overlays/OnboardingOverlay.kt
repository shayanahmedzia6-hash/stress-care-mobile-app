package com.example.ui.overlays

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ColorGSR
import com.example.ui.theme.ColorGSRBgLight
import com.example.ui.theme.ColorHeartRate
import com.example.ui.theme.ColorHeartRateBgLight
import com.example.ui.theme.ColorMotion
import com.example.ui.theme.ColorMotionBgLight
import com.example.ui.theme.ColorSkinTemp
import com.example.ui.theme.ColorSkinTempBgLight
import com.example.ui.theme.LightBgGradEnd
import com.example.ui.theme.LightBgGradMid
import com.example.ui.theme.LightBgGradTop
import com.example.ui.theme.LightPrimary
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.StatusOnlineGreen
import com.example.ui.theme.StatusOnlineGreenBg
import com.example.viewmodel.StressCareViewModel

@Composable
fun OnboardingOverlay(
    viewModel: StressCareViewModel,
    onDismiss: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(
            listOf(
                Color(0xFF0F172A),
                Color(0xFF0B0F19),
                Color(0xFF070B12)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Color(0xFFEAF3FF),
                Color(0xFFF5FAFF),
                Color(0xFFFFFFFF)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("onboarding_overlay")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Logo on left + Skip on right (matching stresscarepp.netlify.app)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "StressCare Logo",
                    modifier = Modifier.height(36.dp),
                    contentScale = ContentScale.Fit
                )

                if (currentStep < 2) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("onboarding_skip_button")
                    ) {
                        Text(
                            text = "Skip",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }

            // Animated Screen Content
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "onboarding_step_content",
                modifier = Modifier.weight(1f)
            ) { step ->
                when (step) {
                    0 -> OnboardingStep1(isDarkMode)
                    1 -> OnboardingStep2(isDarkMode)
                    else -> OnboardingStep3(isDarkMode)
                }
            }

            // Footer: Indicator Dots + Next Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3 Dots indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { idx ->
                        val isActive = idx == currentStep
                        Box(
                            modifier = Modifier
                                .height(9.dp)
                                .width(if (isActive) 24.dp else 9.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(
                                    if (isActive) MaterialTheme.colorScheme.primary
                                    else if (isDarkMode) Color(0xFF334155)
                                    else Color(0xFFCCD9EE)
                                )
                        )
                    }
                }

                // Next Pill Button
                Button(
                    onClick = {
                        if (currentStep < 2) {
                            currentStep++
                        } else {
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("onboarding_next_button"),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (currentStep == 2) "Get Started" else "Next",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingStep1(isDarkMode: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Title Block
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Track Your Stress\nin Real Time",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Monitor heart rate, GSR, skin temperature\nand motion using your wearable device.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        // Illustration with floating metric cards
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding1),
                contentDescription = "Onboarding 1 Illustration",
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Fit
            )

            // Floating Top-Left: Heart Rate
            FloatingMetricCard(
                icon = Icons.Default.Favorite,
                iconColor = ColorHeartRate,
                iconBg = if (isDarkMode) ColorHeartRate.copy(alpha = 0.2f) else ColorHeartRateBgLight,
                value = "72",
                unit = "bpm",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 4.dp, top = 12.dp)
            )

            // Floating Top-Right: GSR
            FloatingMetricCard(
                icon = Icons.Default.Waves,
                iconColor = ColorGSR,
                iconBg = if (isDarkMode) ColorGSR.copy(alpha = 0.2f) else ColorGSRBgLight,
                value = "4.2",
                unit = "µS",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp, top = 12.dp)
            )

            // Floating Bottom-Left: Temp
            FloatingMetricCard(
                icon = Icons.Default.Thermostat,
                iconColor = ColorSkinTemp,
                iconBg = if (isDarkMode) ColorSkinTemp.copy(alpha = 0.2f) else ColorSkinTempBgLight,
                value = "34.5",
                unit = "°C",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 4.dp, bottom = 12.dp)
            )

            // Floating Bottom-Right: Motion
            FloatingMetricCard(
                icon = Icons.AutoMirrored.Filled.DirectionsRun,
                iconColor = ColorMotion,
                iconBg = if (isDarkMode) ColorMotion.copy(alpha = 0.2f) else ColorMotionBgLight,
                value = "0.08",
                unit = "g",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 4.dp, bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun OnboardingStep2(isDarkMode: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Title Block
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Connect Your\nWearable Device",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pair your smart band via Bluetooth to start\ncollecting stress, sleep and migraine-related data.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        // Illustration + BLE badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding2),
                contentDescription = "Onboarding 2 Illustration",
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Fit
            )

            // Pulsing BLE Badge
            Surface(
                shape = CircleShape,
                color = if (isDarkMode) Color(0xFF1E3A8A) else Color(0xFFEEF5FF),
                modifier = Modifier
                    .size(54.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .shadow(8.dp, CircleShape)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Bluetooth,
                        contentDescription = "Bluetooth",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Floating info card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = StatusOnlineGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "ESP32-S3 Low-Latency BLE • 50 Hz",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingStep3(isDarkMode: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Title Block
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Get Sleep &\nMigraine Insights",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Understand sleep quality, stress patterns and\nmigraine risk with personalized AI insights\nand early warnings.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        // Illustration + Insight Badges
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding3),
                contentDescription = "Onboarding 3 Illustration",
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Fit
            )

            // Top Left Insight: Sleep Score
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 4.dp, top = 8.dp)
                    .shadow(6.dp, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bedtime,
                            contentDescription = null,
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Sleep Quality",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "85% Good",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = StatusOnlineGreen
                    )
                }
            }

            // Bottom Right Insight: Migraine Risk
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 4.dp, bottom = 8.dp)
                    .shadow(6.dp, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = StatusOnlineGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Migraine Risk",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "Low Risk (24/100)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = StatusOnlineGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingMetricCard(
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp
            )
        }
    }
}
