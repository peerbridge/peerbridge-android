package com.peerbridge.android.model

import androidx.compose.runtime.Immutable
import com.peerbridge.android.crypto.PublicKey
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Conversation(val partnerPublicKey: PublicKey, val lastTransaction: Transaction?)
