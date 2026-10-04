package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val StressCareLightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = LightPrimaryLight,
    onPrimaryContainer = LightPrimaryDark,
    secondary = ColorSkinTemp,
    onSecondary = Color.White,
    secondaryContainer = ColorSkinTempBgLight,
    onSecondaryContainer = ColorSkinTemp,
    tertiary = ColorGSR,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceHighlight,
    onSurfaceVariant = LightTextSecondary,
    outline = LightCardBorder,
    outlineVariant = LightDivider,
    error = StressHighColor,
    onError = Color.White
)

val StressCareDarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.Black,
    primaryContainer = DarkSurfaceHighlight,
    onPrimaryContainer = DarkPrimary,
    secondary = ColorSkinTemp,
    onSecondary = Color.White,
    secondaryContainer = DarkSurfaceElevated,
    onSecondaryContainer = ColorSkinTemp,
    tertiary = ColorGSR,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = DarkDivider,
    error = StressHighColor,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Defaults to clean modern Light Theme from stresscarepp.netlify.app
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) StressCareDarkColorScheme else StressCareLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val statusBarColor = if (darkTheme) DarkBackground else LightBgGradTop
                val navBarColor = if (darkTheme) DarkBackground else LightSurface
                window.statusBarColor = statusBarColor.toArgb()
                window.navigationBarColor = navBarColor.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
