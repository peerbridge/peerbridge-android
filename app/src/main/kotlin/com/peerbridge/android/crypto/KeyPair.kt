package com.peerbridge.android.crypto

import com.peerbridge.secp256k1.Libsecp256k1

data class KeyPair(val privateKeyHex: String, val publicKeyHex: String) {
    companion object {
        fun create(): KeyPair = Libsecp256k1.createKeyPairHex().let { KeyPair(it.first, it.second) }
    }
}
