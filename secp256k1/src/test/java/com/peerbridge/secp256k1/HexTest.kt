package com.peerbridge.secp256k1

import org.junit.Assert.assertEquals
import org.junit.Test

class HexTest {
    @Test
    fun decodeString() {
        assertEquals("Hello", String(Hex.decodeString("48656c6c6f"), Charsets.UTF_8))
    }

    @Test
    fun encode() {
        assertEquals("48656c6c6f", Hex.encodeToString("Hello".toByteArray(Charsets.UTF_8)))
    }
}
