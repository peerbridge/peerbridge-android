package com.peerbridge.android.date

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

const val ISO8601DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

/**
 * The old Java Date/SimpleDateFormat classes seem to not be ISO8601 compliant.
 * According to ISO8601, if a date string contains a trailing 'Z', it is
 * equivalent to '+0000'.
 * So e.g. 2020-05-26T10:00:00.000Z is equivalent to 2020-05-26T10:00:00.000+0000.
 * This is a feature that is supported by the Datetime class available since Java 8.
 * Unfortunately using Datetime is only supported by Android SDK v.26 and above.
 * This is at the current time of writing (Mai 2020) not feasible.
 * Therefore the following creates a wrapper around the SimpleDateFormat class
 * to provide this functionality. This is done by a simple [Regex] which replaces
 * a and only a trailing 'Z' with '+0000' and parses the date string as usual.
 */
object DateFormatter : SimpleDateFormat(ISO8601DateFormat, Locale.GERMANY) {
    override fun parse(text: String): Date? = super.parse(text.replace(Regex("Z\$"), "+0000"))
    override fun parse(text: String, pos: ParsePosition): Date? = super.parse(text.replace(Regex("Z\$"), "+0000"), pos)
}
