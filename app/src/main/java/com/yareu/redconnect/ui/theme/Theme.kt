package com.yareu.redconnect.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BurgundyPrimary,
    onPrimary = White,
    primaryContainer = PurpleSecondary,
    onPrimaryContainer = LightGray,

    secondary = PurpleSecondary,
    onSecondary = White,
    secondaryContainer = PinkAccent,
    onSecondaryContainer = White,

    tertiary = BlueAccent,
    onTertiary = DarkText,
    tertiaryContainer = BlueAccent,
    onTertiaryContainer = White,

    background = Color(0xFF1A0A0F), // Dark burgundy background
    onBackground = LightGray,

    surface = Color(0xFF2D1419), // Slightly lighter than background
    onSurface = LightGray,
    surfaceVariant = PurpleSecondary,
    onSurfaceVariant = Gray,

    error = ErrorRed,
    onError = White,

    outline = Gray,
    outlineVariant = DarkGray
)

private val LightColorScheme = lightColorScheme(
    primary = BurgundyPrimary,
    onPrimary = White,
    primaryContainer = Color(0xFFFFD9E2), // Light pink container
    onPrimaryContainer = BurgundyPrimary,

    secondary = PurpleSecondary,
    onSecondary = White,
    secondaryContainer = Color(0xFFFFD9E2),
    onSecondaryContainer = PurpleSecondary,

    tertiary = BlueAccent,
    onTertiary = White,
    tertiaryContainer = Color(0xFFD1ECF7), // Light blue container
    onTertiaryContainer = Color(0xFF004C6D),

    background = LightGray,
    onBackground = DarkText,

    surface = White,
    onSurface = DarkText,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray,

    error = ErrorRed,
    onError = White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = Gray,
    outlineVariant = LightGray
)

@Composable
fun RedConnectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color dimatikan karena kita pakai custom branding colors
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}