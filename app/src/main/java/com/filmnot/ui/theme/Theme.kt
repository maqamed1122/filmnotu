package com.filmnot.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Background = Color(0xFF0F0F0F)
val Surface = Color(0xFF1A1A1A)
val SurfaceVariant = Color(0xFF252525)
val Primary = Color(0xFFE50914)       // Netflix-like red
val PrimaryVariant = Color(0xFFB20710)
val OnPrimary = Color(0xFFFFFFFF)
val OnBackground = Color(0xFFFFFFFF)
val OnSurface = Color(0xFFE0E0E0)
val TextSecondary = Color(0xFF9E9E9E)
val StarYellow = Color(0xFFFFC107)
val Divider = Color(0xFF2A2A2A)
val GreenWatched = Color(0xFF4CAF50)
val CardSurface = Color(0xFF1E1E1E)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,
    secondary = Color(0xFF757575),
    tertiary = StarYellow
)

@Composable
fun FilmnotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
