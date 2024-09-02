package com.ganecamp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Green,
    secondary = Black,
    tertiary = White,
    background = White,
    surface = Black,
    onPrimary = Black,
    onSecondary = White,
    onTertiary = LightGray,
    onBackground = Black,
    onSurface = Black,
)

@Composable
fun AndroidGanecampTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}