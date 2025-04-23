package com.TravelPlanner.ui.theme

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



private val LightColorScheme = lightColorScheme(
    primary = Color(0xff46141d), // Bright Blue
    background = Color(0xFFFFFFFF), // Light Gray
    surface = Color(0xFFFFFFFF), // White
    onPrimary = Color(0xFFFFFFFF), // White
    onSecondary = Color(0xFF000000), // Black
    onTertiary = Color(0xFF000000), // Black
    onBackground = Color(0xFF000000), // Black
    onSurface = Color(0xFF000000) // Black
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF), // Light Purple
    background = Color(0xff46141d), // Dark Gray
    surface = Color(0xFF1E1E1E), // Slightly lighter Dark Gray
    onPrimary = Color(0xff46141d), // Black
    onSecondary = Color(0xFF39080F), // Black
    onTertiary = Color(0xFF000000), // Black
    onBackground = Color(0xFFFFFFFF), // White
    onSurface = Color(0xFFFFFFFF) // White
)

@Composable
fun APPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic color for testing
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