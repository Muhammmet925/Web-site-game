package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val TacticalDarkColorScheme = darkColorScheme(
    primary = CyanNeon,
    onPrimary = SpaceBlack,
    primaryContainer = SurfaceElevated,
    onPrimaryContainer = CyanGlow,
    secondary = AmberGold,
    onSecondary = SpaceBlack,
    secondaryContainer = SurfaceDark,
    onSecondaryContainer = AmberLight,
    tertiary = MatrixGreen,
    onTertiary = SpaceBlack,
    background = SpaceBlack,
    onBackground = TextPrimary,
    surface = DarkNavy,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder,
    error = DangerRed,
    onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep high-contrast tactical sci-fi styling
    content: @Composable () -> Unit
) {
    val colorScheme = TacticalDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = SpaceBlack.toArgb()
                window.navigationBarColor = SpaceBlack.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
