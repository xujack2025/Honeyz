package com.example.honeyz.ui.theme

import android.app.Activity
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
    tertiary = CustomPrimaryLight,     // You can use different tertiary color
    surface = CustomSurface,           // Surface color for dark mode
    error = CustomError,               // Error color
    onPrimary = Color.White,           // Text color on primary
    onSecondary = Color.Black,         // Text color on secondary
    onTertiary = Color.White,          // Text color on tertiary
    onBackground = Color.White,        // Text color on background
    onSurface = Color.White,           // Text color on surface
    onError = Color.White,              // Text color on error
    primary = light_primary,
    secondary = light_secondary,
    background = light_primary_background,
    primaryContainer = light_primary_background,
    secondaryContainer = light_primary_background,
)

private val LightColorScheme = lightColorScheme(
    tertiary = CustomPrimaryLight,     // Tertiary color
    surface = CustomSurface,           // Surface color for light mode
    error = CustomError,               // Error color
    onPrimary = Color.White,           // Text color on primary
    onSecondary = Color.Black,         // Text color on secondary
    onTertiary = Color.Black,          // Text color on tertiary
    onBackground = Color.Black,        // Text color on background
    onSurface = Color.Black,           // Text color on surface
    onError = Color.White,              // Text color on error
    primary = light_primary,
    secondary = light_secondary,
    background = light_primary_background,
    primaryContainer = light_primary_background,
    secondaryContainer = light_primary_background,
)

@Composable
fun MaterialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
        typography = Typography,  // Use your custom typography if needed
        content = content
    )
}