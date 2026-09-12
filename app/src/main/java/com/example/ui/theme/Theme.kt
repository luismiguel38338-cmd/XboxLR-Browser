package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = XboxBrightGreen,
    onPrimary = Color(0xFF003914),
    primaryContainer = XboxDarkGreen,
    onPrimaryContainer = XboxNeonGreen,
    secondary = XboxAccentLime,
    onSecondary = Color(0xFF003914),
    secondaryContainer = Color(0xFF194025),
    onSecondaryContainer = Color(0xFFA5F7B8),
    tertiary = Color(0xFF4EE2C0),
    onTertiary = Color(0xFF00382B),
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkSurfaceBorder,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = XboxGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC7F3D0),
    onPrimaryContainer = Color(0xFF002107),
    secondary = Color(0xFF1E6E38),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD4EED8),
    onSecondaryContainer = Color(0xFF07210E),
    tertiary = Color(0xFF0D7A64),
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightSurfaceBorder,
    error = ErrorRed,
    onError = Color.White
)

private val IncognitoColorScheme = darkColorScheme(
    primary = IncognitoPurple,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF381E72),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFFD0BCFF),
    onSecondary = Color(0xFF381E72),
    background = Color(0xFF0D0A14),
    onBackground = Color(0xFFE8E0F0),
    surface = IncognitoDarkSurface,
    onSurface = Color(0xFFE8E0F0),
    surfaceVariant = Color(0xFF1E1630),
    onSurfaceVariant = Color(0xFFAAA0B8),
    outline = IncognitoBorder,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun XboxLRTheme(
    themeMode: com.example.data.local.ThemeMode = com.example.data.local.ThemeMode.SYSTEM,
    darkTheme: Boolean = isSystemInDarkTheme(),
    isIncognito: Boolean = false,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        com.example.data.local.ThemeMode.DARK -> true
        com.example.data.local.ThemeMode.LIGHT -> false
        com.example.data.local.ThemeMode.SYSTEM -> darkTheme
    }

    val colorScheme = when {
        isIncognito -> IncognitoColorScheme
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
