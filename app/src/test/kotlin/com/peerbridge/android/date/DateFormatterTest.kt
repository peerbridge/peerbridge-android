package com.peerbridge.android.date

import org.junit.Assert.*

import org.junit.Test
import java.text.ParsePosition

class DateFormatterTest {
    @Test
    fun parse() {
        val dateOfOpeningImplicit = DateFormatter.parse("2017-06-15T11:00:00.000Z")
        val dateOfOpeningExplicit = DateFormatter.parse("2017-06-15T11:00:00.000+0000")
        assertEquals(dateOfOpeningExplicit, dateOfOpeningImplicit)
    }

    @Test
    fun parseWithParsePosition() {
        val dateOfOpeningImplicit1 = DateFormatter.parse("2017-06-15T11:00:00.000Z", ParsePosition(0))
        val dateOfOpeningImplicit2 = DateFormatter.parse("Z2017-06-15T11:00:00.000Z", ParsePosition(1))
        val dateOfOpeningImplicit3 = DateFormatter.parse("Z2017-06-15T11:00:00.000+0000", ParsePosition(1))
        val dateOfOpeningExplicit = DateFormatter.parse("2017-06-15T11:00:00.000+0000", ParsePosition(0))
        assertEquals(dateOfOpeningExplicit, dateOfOpeningImplicit1)
        assertEquals(dateOfOpeningExplicit, dateOfOpeningImplicit2)
        assertEquals(dateOfOpeningExplicit, dateOfOpeningImplicit3)
    }
}
