package com.example.pawsy.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00325E),
    secondary = Color(0xFFFFB088),
    tertiary = Color(0xFFFFA477),

    background = Color(0xFFFFF9F0),
    surface = Color(0xFFFF6F61),

    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7FB3E8),
    secondary = Color(0xFFFFB088),
    tertiary = Color(0xFFFFA477),

    background = Color(0xFF1E1B18),
    surface = Color(0xFF222A58),

    onBackground = Color(0xFFF5F1EC),
    onSurface = Color(0xFFE6E1E5)
)

val LocalPawsyColors = staticCompositionLocalOf {
    LightPawsyColors
}

@Composable
fun PawsyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val pawsyColors = if (darkTheme) {
        DarkPawsyColors
    } else {
        LightPawsyColors
    }

    CompositionLocalProvider(
        LocalPawsyColors provides pawsyColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val MaterialTheme.pawsyColors: PawsyColors
    @Composable
    get() = LocalPawsyColors.current