package com.bzapata.triangle.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8B28F7),
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xff262626),
    surface = Color(0xff1f1f1f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    //onBackground = Color(0xFF1C1B1F),
    //onSurface = Color(0xFF1C1B1F),
)


@Composable
fun TriangleTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}