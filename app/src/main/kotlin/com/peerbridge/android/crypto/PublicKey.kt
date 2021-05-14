package com.peerbridge.android.crypto

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
inline class PublicKey(val value: String) {
    val short: String
        get() = value.substring(0..6)
}
