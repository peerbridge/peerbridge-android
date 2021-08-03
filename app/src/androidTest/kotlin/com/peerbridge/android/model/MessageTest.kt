package com.peerbridge.android.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.peerbridge.android.crypto.KeyPair
import com.peerbridge.android.crypto.PublicKey
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MessageInstrumentedTest {
    @Test
    fun send() = runBlocking {
        val keyPair = KeyPair.create()
        val publicKey = PublicKey("0300db96ed8ea9e16350a16a7d01126ce6f00e6917cd4b2e70f838d159f653b510")
        val message = TokenMessage("abcd")
        val transaction = message.send(keyPair, publicKey)
        assertNotNull(transaction)
        print("$transaction")
    }
}
