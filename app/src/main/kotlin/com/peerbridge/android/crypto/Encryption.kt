package com.peerbridge.android.crypto

import com.peerbridge.secp256k1.Hex
import com.peerbridge.secp256k1.Libsecp256k1
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object Encryption {
    @Suppress("unused")
    private fun createRandomSymmetricKey(): SymmetricKey {
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(Nonce)

        val key = ByteArray(AESKeySize / ByteSize)
        secureRandom.nextBytes(key) // fill 256 bit of random data

        return SymmetricKey(key)
    }

    fun secret(privateKeyHex: String, publicKeyHex: String): SymmetricKey? {
        val keyHex = Libsecp256k1.computeSecretHex(privateKeyHex, publicKeyHex)
        return keyHex?.let { SymmetricKey(Hex.decodeString(it)) }
    }

    fun encrypt(data: ByteArray, key: SymmetricKey): ByteArray {
        val keySpec = SecretKeySpec(key.value, "AES")
        val gcmSpec = GCMParameterSpec(GCMAuthenticationTagSize, Nonce)
        return AESCipher.run {
            init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
            iv.plus(doFinal(data))
        }
    }

    fun decrypt(data: ByteArray, key: SymmetricKey): ByteArray {
        val keySpec = SecretKeySpec(key.value, "AES")
        val iv = data.sliceArray(0..11)
        val encrypted = data.sliceArray(12 until data.size)
        val gcmSpec = GCMParameterSpec(GCMAuthenticationTagSize, iv)
        return AESCipher.run {
            init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
            doFinal(encrypted)
        }
    }

    private const val ByteSize = 8 // Bits

    private const val AESKeySize = 256 // Bits

    private const val GCMAuthenticationTagSize = 128 // Bits

    private const val IVBufferSize = 96 // Bits

    private val Nonce: ByteArray
        get() = ByteArray(IVBufferSize / ByteSize)

    private val AESCipher: Cipher
        get() = Cipher.getInstance("AES/GCM/NoPadding")
}
