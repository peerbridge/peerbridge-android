package com.peerbridge.android.crypto

import android.util.Base64
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.peerbridge.secp256k1.Hex
import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptionTest {
    private val message = "{\"type\":\"content\",\"content\":\"Hallo Welt\"}"
    private val privateKeyHex = "a32da27d8aff2bcfd159e6a61d9fe13da6cf426bf19c7feb2b4e0d0d914d4d06"
    private val publicKeyHex = "0300db96ed8ea9e16350a16a7d01126ce6f00e6917cd4b2e70f838d159f653b510"
    private val sharedSecret = "985506da2199a728043f716f06961411969b79368fd2a621b99f03d07bf6c986"
    private val encryptedMessage = "CqpDRcO8fBJTT610RwdyyRagauIdgATEFBNTskBQOM4Il6h5Hi4aDtNT5H+/ppjiZN50PFtq7fqvA9xgHyr5vjQNOBg/"

    @Test
    fun secret() {
        val secret = Encryption.secret(privateKeyHex, publicKeyHex)
        assertNotNull(secret)
        assertEquals(sharedSecret, Hex.encodeToString(secret!!.value))
    }

    @Test
    fun encrypt() {
        val secret = Encryption.secret(privateKeyHex, publicKeyHex)
        val messageData = message.toByteArray(Charsets.UTF_8)
        val encryptedData = Encryption.encrypt(messageData, secret!!)
        val decryptedMessageData = Encryption.decrypt(encryptedData, secret)
        assertEquals(message, String(decryptedMessageData, Charsets.UTF_8))
    }

    @Test
    fun decrypt() {
        val secret = Encryption.secret(privateKeyHex, publicKeyHex)
        val encryptedData = Base64.decode(encryptedMessage, Base64.DEFAULT)
        val messageData = Encryption.decrypt(encryptedData, secret!!)
        assertEquals(message, String(messageData, Charsets.UTF_8))
    }
}
