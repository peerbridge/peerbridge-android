package com.peerbridge.android.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Dp

val Blue500 = Color(0xff009cff)
val Blue600 = Color(0xff1b59bc)

val LightBlue500 = Color(0xff00b0ff)
val LightBlue600 = Color(0xff0091ea)

val Red300 = Color(0xFFEA6D7E)
val Red800 = Color(0xFFD00036)

val Grey50 = Color(0xfffafafa)
val Grey100 = Color(0xfff5f5f5)
val Grey300 = Color(0xffe0e0e0)
val Grey600 = Color(0xff757575)
val Grey800 = Color(0xff424242)
val Grey850 = Color(0xff303030)
val Grey900 = Color(0xff212121)

val blueGradient = Brush.linearGradient(
    listOf(Color(0xff00c6ff), Color(0xff0072ff)),
    Offset(0.0f, Float.POSITIVE_INFINITY),
    Offset(Float.POSITIVE_INFINITY, 0.0f)
)
