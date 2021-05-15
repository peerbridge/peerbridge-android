package com.peerbridge.secp256k1

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class Libsecp256k1Test {

    @Test
    fun createKeyPair() {
        val (privateKey, publicKey) = Libsecp256k1.createKeyPair()
        assertTrue(privateKey.size == 32)
        assertTrue(publicKey.size == 33)
    }

    @Test
    fun createKeyPairHex() {
        val (privateKeyHex, publicKeyHex) = Libsecp256k1.createKeyPairHex()
        assertTrue(privateKeyHex.length == 32 * 2)
        assertTrue(publicKeyHex.length == 33 * 2)
    }

    @Test
    fun computePublicKey() {
        val (privateKey, publicKey) = Libsecp256k1.createKeyPair()
        val computedPulicKey = Libsecp256k1.computePublicKey(privateKey)
        assertNotNull(computedPulicKey)
        assertEquals(publicKey.size, computedPulicKey!!.size)
        for ((i, byte) in computedPulicKey.withIndex()) {
            assertEquals(publicKey[i], byte)
        }
    }

    @Test
    fun computePublicKeyHex() {
        val (privateKeyHex, publicKeyHex) = Libsecp256k1.createKeyPairHex()
        val computedPulicKeyHex = Libsecp256k1.computePublicKeyHex(privateKeyHex)
        assertNotNull(computedPulicKeyHex)
        assertEquals(publicKeyHex.length, computedPulicKeyHex!!.length)
        assertEquals(publicKeyHex, computedPulicKeyHex)
    }

    @Test
    fun computeSecret() {
        val privateKeyHex = "a32da27d8aff2bcfd159e6a61d9fe13da6cf426bf19c7feb2b4e0d0d914d4d06"
        val privateKey = Hex.decodeString(privateKeyHex)

        val publicKeyHex = "0300db96ed8ea9e16350a16a7d01126ce6f00e6917cd4b2e70f838d159f653b510"
        val publicKey = Hex.decodeString(publicKeyHex)

        val secretHex = "985506da2199a728043f716f06961411969b79368fd2a621b99f03d07bf6c986"
        val secret = Hex.decodeString(secretHex)

        val computedSecret = Libsecp256k1.computeSecret(privateKey, publicKey)
        assertNotNull(computedSecret)
        assertEquals(secret.size, computedSecret!!.size)
        for ((i, byte) in computedSecret.withIndex()) {
            assertEquals(secret[i], byte)
        }
    }

    @Test
    fun computeSecretHex() {
        val privateKeyHex = "a32da27d8aff2bcfd159e6a61d9fe13da6cf426bf19c7feb2b4e0d0d914d4d06"
        val publicKeyHex = "0300db96ed8ea9e16350a16a7d01126ce6f00e6917cd4b2e70f838d159f653b510"
        val secretHex = "985506da2199a728043f716f06961411969b79368fd2a621b99f03d07bf6c986"
        val computedSecret = Libsecp256k1.computeSecretHex(privateKeyHex, publicKeyHex)
        assertNotNull(computedSecret)
        assertEquals(secretHex.length, computedSecret!!.length)
        assertEquals(secretHex, computedSecret)
    }

    @Test
    fun sign() {
        val keyHex = "60f8700baf057e6131b912b97f2e36f54a67544a5f4659de348e988306ab1a3f"
        val key = Hex.decodeString(keyHex)

        val msgHex = "185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969"
        val msg = Hex.decodeString(msgHex)

        val sigHex = "b97676fd0290f9b98c02e0bf11c495af25fd4126a63e46ec90907c15ae7ff30a727e97bf424f31d067becdcc5a0549441e9a918bbe6defc2dcaea52021bab267"
        val sig = Hex.decodeString(sigHex)

        var computedSignature = Libsecp256k1.sign(key, msg)
        assertNotNull(computedSignature)

        // Remove recovery byte
        computedSignature = computedSignature!!.sliceArray(0 until computedSignature.size-1)

        assertEquals(sig.size, computedSignature.size)
        for ((i, byte) in computedSignature.withIndex()) {
            assertEquals(sig[i], byte)
        }
    }

    @Test
    fun signHex() {
        val keyHex = "60f8700baf057e6131b912b97f2e36f54a67544a5f4659de348e988306ab1a3f"
        val msgHex = "185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969"
        val sigHex = "b97676fd0290f9b98c02e0bf11c495af25fd4126a63e46ec90907c15ae7ff30a727e97bf424f31d067becdcc5a0549441e9a918bbe6defc2dcaea52021bab267"
        val computedSignatureHex = Libsecp256k1.signHex(keyHex, msgHex)
        assertNotNull(computedSignatureHex)
        assertEquals(sigHex.length, computedSignatureHex!!.length-2)
        assertEquals(sigHex, computedSignatureHex.substring(0 until computedSignatureHex.length-2))
    }

    @Test
    fun verifySignature() {
        val keyHex = "02caa8bded7764cca5bde64c10ae54fc91f4bcd2de08eb4c66b1e2dc3d9dd5519d"
        val key = Hex.decodeString(keyHex)

        val msgHex = "185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969"
        val msg = Hex.decodeString(msgHex)

        val sigHex = "b97676fd0290f9b98c02e0bf11c495af25fd4126a63e46ec90907c15ae7ff30a727e97bf424f31d067becdcc5a0549441e9a918bbe6defc2dcaea52021bab267"
        val sig = Hex.decodeString(sigHex)

        val isSignatureValid = Libsecp256k1.verifySignature(key, msg, sig)
        assertTrue(isSignatureValid)
    }

    @Test
    fun verifySignatureHex() {
        val keyHex = "02caa8bded7764cca5bde64c10ae54fc91f4bcd2de08eb4c66b1e2dc3d9dd5519d"
        val msgHex = "185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969"
        val sigHex = "b97676fd0290f9b98c02e0bf11c495af25fd4126a63e46ec90907c15ae7ff30a727e97bf424f31d067becdcc5a0549441e9a918bbe6defc2dcaea52021bab267"
        val isSignatureValid = Libsecp256k1.verifySignatureHex(keyHex, msgHex, sigHex)
        assertTrue(isSignatureValid)
    }
}
