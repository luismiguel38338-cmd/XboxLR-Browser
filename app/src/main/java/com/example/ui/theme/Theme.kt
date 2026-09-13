package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NovaBrightCyan,
    onPrimary = Color(0xFF00324B),
    primaryContainer = Color(0xFF075985),
    onPrimaryContainer = NovaCyan,
    secondary = NovaAccentIndigo,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF312E81),
    onSecondaryContainer = Color(0xFFC7D2FE),
    tertiary = Color(0xFF38BDF8),
    onTertiary = Color(0xFF003544),
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
    primary = NovaElectricBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = NovaAccentIndigo,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEF2FF),
    onSecondaryContainer = Color(0xFF3730A3),
    tertiary = Color(0xFF0284C7),
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
fun NovaTheme(
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

@Composable
fun XboxLRTheme(
    themeMode: com.example.data.local.ThemeMode = com.example.data.local.ThemeMode.SYSTEM,
    darkTheme: Boolean = isSystemInDarkTheme(),
    isIncognito: Boolean = false,
    content: @Composable () -> Unit
) {
    NovaTheme(
        themeMode = themeMode,
        darkTheme = darkTheme,
        isIncognito = isIncognito,
        content = content
    )
}
