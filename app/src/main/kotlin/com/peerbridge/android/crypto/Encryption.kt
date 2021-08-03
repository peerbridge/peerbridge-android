package com.peerbridge.android.crypto

import com.peerbridge.secp256k1.Hex
import com.peerbridge.secp256k1.Libsecp256k1
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object Encryption {
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    val random: String
        get() = Hex.encodeToString((0 until 256/ByteSize)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
            .sha256)

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
        val iv = data.sliceArray(0 until NonceSize)
        val encrypted = data.sliceArray(NonceSize until data.size)
        val gcmSpec = GCMParameterSpec(GCMAuthenticationTagSize, iv)
        return AESCipher.run {
            init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
            doFinal(encrypted)
        }
    }

    private const val GCMAuthenticationTagSize = 128 // Bits

    private const val ByteSize = 8 // Bits

    private const val IVBufferSize = 96 // Bits

    private const val NonceSize = IVBufferSize / ByteSize // Bytes

    private val Nonce: ByteArray
        get() = ByteArray(NonceSize).apply { SecureRandom().nextBytes(this) }

    private val AESCipher: Cipher
        get() = Cipher.getInstance("AES/GCM/NoPadding")
}
