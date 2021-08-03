package com.peerbridge.android.ui.view

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.peerbridge.android.R
import com.peerbridge.android.data.keyPair
import com.peerbridge.android.ui.component.PeerBridgeIcon
import com.peerbridge.android.ui.component.PeerBridgeAppBar
import com.peerbridge.android.ui.context.LocalKeyPair
import com.google.zxing.BarcodeFormat

import android.graphics.Bitmap
import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.common.BitMatrix
import com.peerbridge.android.ui.context.PreviewKeyPairProvider
import com.peerbridge.android.ui.theme.*

private fun encodeAsQRCode(text: String, size: Int = 512): ImageBitmap {
    val writer = QRCodeWriter()
    val matrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(matrix.width, matrix.height, Bitmap.Config.RGB_565)
    for (x in 0 until matrix.width) {
        for (y in 0 until matrix.height) {
            val color = if (matrix.get(x, y)) parseColor("#ff303030") else parseColor("#ffffffff")
            bitmap.setPixel(x, y, color)
        }
    }
    return bitmap.asImageBitmap()
}

@Composable
fun Pair(navController: NavHostController, onShare: (intent: Intent) -> Unit = {}) {
    val (_, publicKeyHex) = LocalKeyPair.current
    val share = "peerbridge://pair?publicKey=${publicKeyHex}"
    val title = stringResource(id = R.string.user_contact)

    Column(modifier = Modifier.fillMaxSize()) {
        PeerBridgeAppBar {
            PeerBridgeIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(id = R.string.back),
                modifier = Modifier.clickable { navController.navigateUp() },
            )
            Text(text = stringResource(id = R.string.user_code), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h5)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.padding(horizontal = 24.dp),
                backgroundColor = if (MaterialTheme.colors.isLight) Grey100 else Grey800,
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = publicKeyHex.substring(0..6),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h4
                    )
                    Text(
                        text = stringResource(id = R.string.user_contact),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption
                    )
                    Card(
                        modifier = Modifier.padding(24.dp),
                        elevation = 0.dp
                    ) {
                        Image(
                            bitmap = encodeAsQRCode(share),
                            contentDescription = stringResource(id = R.string.user_code),
                            modifier = Modifier
                                .height(256.dp)
                                .width(256.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                border = BorderStroke(width = 2.dp, brush = blueGradient)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = publicKeyHex,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1F)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    PeerBridgeIcon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_share),
                        contentDescription = stringResource(id = R.string.back),
                        modifier = Modifier.clickable {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TITLE, title)
                                putExtra(Intent.EXTRA_TEXT, publicKeyHex)
                            }

                            val shareIntent = Intent.createChooser(sendIntent, null)
                            onShare(shareIntent)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PairPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    PreviewKeyPairProvider {
        PeerBridgeTheme(darkTheme = isDarkTheme) {
            Surface(color = MaterialTheme.colors.background) {
                val navController = rememberNavController()
                Pair(navController)
            }
        }
    }
}
