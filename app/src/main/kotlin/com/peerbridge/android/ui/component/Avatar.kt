package com.peerbridge.android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.peerbridge.android.crypto.md5
import com.peerbridge.android.data.SamplePublicKeyProvider
import com.peerbridge.android.ui.theme.PeerBridgeTheme
import com.peerbridge.android.ui.theme.Blue500
import com.peerbridge.android.ui.theme.ThemedPreviewParameterProvider

@Composable
fun Avatar(publicKeyHex: String, modifier: Modifier = Modifier, size: Dp = 32.dp) {
    val grid = publicKeyHex.md5.asUByteArray().map { it.toInt() / 255f }.chunked(4) // 4x4 Grid

    Column(modifier = modifier) {
        grid.forEachIndexed { rowIndex, row ->
            key(rowIndex) {
                Row(modifier = Modifier.padding(all = 1.dp)) {
                    for ((cellIndex, cell) in row.withIndex()) {
                        key(cellIndex) {
                            val color = Blue500.copy(alpha = cell)
                            val shape = RoundedCornerShape(2.5.dp)
                            Box(
                                modifier = Modifier
                                    .padding(all = 1.dp)
                                    .size(size/4)
                                    .clip(shape)
                                    .background(color)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AvatarPreview(@PreviewParameter(SamplePublicKeyProvider::class) params: Pair<String, Boolean>) {
    val (publicKeyHex, isDarkTheme) = params
    PeerBridgeTheme(darkTheme = isDarkTheme) {
        Surface(color = MaterialTheme.colors.background) {
            Avatar(publicKeyHex)
        }
    }
}
