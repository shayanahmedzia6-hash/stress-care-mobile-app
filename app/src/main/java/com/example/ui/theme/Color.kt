package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Light Palette (from stresscarepp.netlify.app)
val LightPrimary = Color(0xFF2563EB)              // #2563eb Royal Blue
val LightPrimaryDark = Color(0xFF1D4ED8)          // #1d4ed8
val LightPrimaryLight = Color(0xFFDBEAFE)         // #dbeafe
val LightBackground = Color(0xFFF5F8FF)           // #f5f8ff
val LightBgGradTop = Color(0xFFDEEEFF)            // #deeeff
val LightBgGradMid = Color(0xFFF0F6FF)            // #f0f6ff
val LightBgGradEnd = Color(0xFFF5F8FF)            // #f5f8ff
val LightSurface = Color(0xFFFFFFFF)              // Pure White card
val LightSurfaceElevated = Color(0xFFFFFFFF)
val LightSurfaceHighlight = Color(0xFFEEF5FF)
val LightCardBorder = Color(0xFFE2E8F0)
val LightDivider = Color(0xFFE5E7EB)

// Light Text Colors (from stresscarepp.netlify.app)
val LightTextPrimary = Color(0xFF1A2B4B)          // #1a2b4b Deep Navy
val LightTextSecondary = Color(0xFF5A6882)        // #5a6882 Slate Grey
val LightTextMuted = Color(0xFF9AADC9)            // #9aadc9 Light Slate

// Dark Palette Foundations
val DarkPrimary = Color(0xFF38BDF8)
val DarkBackground = Color(0xFF0B0F19)
val DarkSurface = Color(0xFF131B2E)
val DarkSurfaceElevated = Color(0xFF1E293B)
val DarkSurfaceHighlight = Color(0xFF27354E)
val DarkBorder = Color(0xFF2C394F)
val DarkDivider = Color(0xFF1F2937)

// Dark Text Colors
val DarkTextPrimary = Color(0xFFF8FAFC)
val DarkTextSecondary = Color(0xFF94A3B8)
val DarkTextMuted = Color(0xFF64748B)

// Sensor & Clinical Metric Colors (shared across themes)
val ColorHeartRate = Color(0xFFEF4444)            // MAX30102 PPG Red
val ColorHeartRateBgLight = Color(0xFFFEE2E2)
val ColorGSR = Color(0xFF0D9488)                  // Grove GSR Teal
val ColorGSRBgLight = Color(0xFFCCFBF1)
val ColorSkinTemp = Color(0xFF7C3AED)             // MLX90614 Temp Purple
val ColorSkinTempBgLight = Color(0xFFEDE9FE)
val ColorMotion = Color(0xFF2563EB)               // MPU6050 Motion Blue
val ColorMotionBgLight = Color(0xFFDBEAFE)

// Stress Level States
val StressLowColor = Color(0xFF16A34A)
val StressModerateColor = Color(0xFFF59E0B)
val StressHighColor = Color(0xFFEF4444)

// Migraine Risk Colors (Light Theme from stresscarepp.netlify.app)
val MigraineCardBgStart = Color(0xFFE6FDF5)
val MigraineCardBgEnd = Color(0xFFD1FAE5)
val MigraineBorderLight = Color(0xFFA7F3D0)
val MigraineTextLight = Color(0xFF047857)
val MigraineSubTextLight = Color(0xFF059669)

val MigraineLow = Color(0xFF16A34A)
val MigraineMed = Color(0xFFF59E0B)
val MigraineHigh = Color(0xFFEF4444)

// Online & Battery
val StatusOnlineGreen = Color(0xFF22C55E)
val StatusOnlineGreenBg = Color(0xFFE8FAF2)

// Compatibility Tokens for components
val CyanPrimary = LightPrimary
val CyanPrimaryVariant = LightPrimaryDark
val PurpleAccent = ColorSkinTemp
val PurpleAccentDark = Color(0xFF6D28D9)
val EmeraldAccent = StatusOnlineGreen
val RosePulse = ColorHeartRate
val AmberWarning = Color(0xFFF59E0B)
val OrangeFlame = Color(0xFFF97316)
val TextPrimary = LightTextPrimary
val TextSecondary = LightTextSecondary
val TextMuted = LightTextMuted
