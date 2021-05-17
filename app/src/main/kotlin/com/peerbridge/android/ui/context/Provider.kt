package com.peerbridge.android.ui.context

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable

@Composable
fun ComponentActivity.PeerBridgeRoot(content: @Composable() () -> Unit) {
    PeerBridgeKeyPairProvider {
        PeerBridgeDatabaseProvider {
            content()
        }
    }
}
