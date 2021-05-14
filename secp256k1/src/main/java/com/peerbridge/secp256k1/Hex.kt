package com.peerbridge.secp256k1

private const val HEX_CHARS = "0123456789abcdef"

object Hex {
    fun decodeString(s: String): ByteArray {
        val b = ByteArray(s.length / 2)

        for (i in s.indices step 2) {
            val j = HEX_CHARS.indexOf(s[i]) shl 4
            val k = HEX_CHARS.indexOf(s[i + 1])
            b[i shr 1] = (j or k).toByte()
        }

        return b
    }

    fun encodeToString(b: ByteArray) : String {
        val s = StringBuilder(b.size * 2)

        b.forEach {
            val i = it.toInt()
            s.append(HEX_CHARS[i shr 4 and 0x0f])
            s.append(HEX_CHARS[i and 0x0f])
        }

        return s.toString()
    }
}
