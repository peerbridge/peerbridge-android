package com.peerbridge.android.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.peerbridge.android.R
import com.peerbridge.android.ui.theme.PeerBridgeTheme
import com.peerbridge.android.ui.theme.ThemePreviewParameterProvider

@Composable
fun PeerBridgeAppBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = AppBarDefaults.TopAppBarElevation,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
        content = content
    )
}

@Preview
@Composable
fun PeerBridgeAppBarPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    PeerBridgeTheme(darkTheme = isDarkTheme) {
        Surface(color = MaterialTheme.colors.background) {
            PeerBridgeAppBar {
                Text("Preview!")
            }
        }
    }
}
