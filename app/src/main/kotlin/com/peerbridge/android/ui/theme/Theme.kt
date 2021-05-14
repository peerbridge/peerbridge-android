package com.peerbridge.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

private val DarkColorPalette = darkColors(
    primary = LightBlue500,
    primaryVariant = LightBlue600,
    secondary = Blue500,
    secondaryVariant = Blue600,
    background = Grey900,
    surface = Grey900,
    error = Red300,
    onPrimary = Grey850,
    onSecondary = Grey850,
    onBackground = Grey50,
    onSurface = Grey50,
    onError = Grey850
)

private val LightColorPalette = lightColors(
    primary = Blue500,
    primaryVariant = Blue600,
    secondary = LightBlue500,
    secondaryVariant = LightBlue600,
    background = Color.White,
    surface = Color.White,
    error = Red800,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Grey850,
    onSurface = Grey850,
    onError = Color.White
)

@Composable
fun PeerBridgeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

class ThemePreviewParameterProvider: PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(false, true)
}

abstract class ThemedPreviewParameterProvider<T>: PreviewParameterProvider<Pair<T, Boolean>> {
    abstract val items: Sequence<T>

    override val values: Sequence<Pair<T, Boolean>>
        get() = items.map { sequenceOf(it to false, it to true) }.flatten()
}
