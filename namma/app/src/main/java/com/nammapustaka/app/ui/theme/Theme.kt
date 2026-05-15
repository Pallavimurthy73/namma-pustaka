package com.nammapustaka.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Saffron,
    secondary = Gold,
    background = Cream,
    surface = androidx.compose.ui.graphics.Color.White,
    error = Danger,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = Ink,
    onBackground = Ink,
    onSurface = Ink,
    onError = androidx.compose.ui.graphics.Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Saffron,
    secondary = Gold,
    background = Cream,
    surface = androidx.compose.ui.graphics.Color.White,
    error = Danger,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = Ink,
    onBackground = Ink,
    onSurface = Ink,
    onError = androidx.compose.ui.graphics.Color.White,
)

private val AppShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(28.dp),
)

@Composable
fun NammaPustakaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content,
    )
}
