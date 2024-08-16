package com.ganecamp.ui.theme

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
    primary = Green,
    secondary = Black,
    tertiary = DarkGray,
    background = White,
    surface = White,
    onPrimary = Black,
    onSecondary = DarkGray,
    onTertiary = Black,
    onBackground = Black,
    onSurface = Black,
//    onSurfaceVariant = White
)

private val LightColorScheme = lightColorScheme(
    primary = Green,
    secondary = Black,
    tertiary = DarkGray,
    background = White,
    surface = Black,
    onPrimary = Black,
    onSecondary = DarkGray,
    onTertiary = LightGray,
    onBackground = Black,
    onSurface = Black,
//    onSurfaceVariant = White
)

@Composable
fun AndroidGanecampTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
        typography = Typography,
        content = content
    )
}