package se.tattooink.powerfy.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PowerfyColorScheme = lightColorScheme(
    primary = PowerfyPrimary,
    background = PowerfyBackground,
    surface = PowerfySurface,
    onSurface = PowerfyTextSecondary,
    outline = PowerfyBorder
)

@Composable
fun PowerfyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PowerfyColorScheme,
        typography = PowerfyTypography,
        shapes = PowerfyShapes,
        content = content
    )
}
