package com.peerbridge.android.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.peerbridge.android.data.SamplePublicKeyProvider
import com.peerbridge.android.crypto.PublicKey
import com.peerbridge.android.ui.component.Avatar
import com.peerbridge.android.ui.component.PeerBridgeAppBar
import com.peerbridge.android.ui.theme.PeerBridgeTheme

@Immutable
data class ProfileState(val publicKey: PublicKey)

@Composable
fun Profile(navController: NavHostController, state: ProfileState) {
    Column(modifier = Modifier.fillMaxSize()) {
        PeerBridgeAppBar {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(id = R.string.add),
                modifier = Modifier
                    .clickable { navController.navigateUp() }
                    .padding(horizontal = 6.dp)
                    .width(24.dp)
                    .height(24.dp),
                tint = MaterialTheme.colors.onSurface
            )
            Text(text = state.publicKey.short, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h5)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Avatar(publicKeyHex = state.publicKey.value, modifier = Modifier.scale(2F))
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = state.publicKey.value,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Preview
@Composable
fun ProfilePreview(@PreviewParameter(SamplePublicKeyProvider::class) params: Pair<PublicKey, Boolean>) {
    val (key, isDarkTheme) = params
    PeerBridgeTheme(darkTheme = isDarkTheme) {
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            Profile(navController, ProfileState(key))
        }
    }
}
