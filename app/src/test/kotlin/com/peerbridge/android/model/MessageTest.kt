package com.peerbridge.android.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class MessageTest {
    private val serializedMessage = "{\"type\":\"content\",\"content\":\"Hallo Welt\"}"

    @Test
    fun encode() {
        val msg: Message = ContentMessage("Hallo Welt")
        assertEquals(serializedMessage, Json.encodeToString(msg))
    }

    @Test
    fun decode() {
        val msg: Message = Json.decodeFromString(serializedMessage)
        assertTrue(msg is ContentMessage)
        assertEquals("Hallo Welt", (msg as ContentMessage).content)
    }
}
