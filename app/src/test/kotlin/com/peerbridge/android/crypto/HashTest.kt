package com.peerbridge.android.crypto

import com.peerbridge.secp256k1.Hex
import org.junit.Assert.*
import org.junit.Test

class HashTest {
    @Test
    fun getMd5() {
        assertEquals(Hex.encodeToString("Foo".md5), "1356c67d7ad1638d816bfb822dd2c25d")
    }

    @Test
    fun getSha1() {
        assertEquals(Hex.encodeToString("Foo".sha1), "201a6b3053cc1422d2c3670b62616221d2290929")
    }

    @Test
    fun getSha256() {
        assertEquals(Hex.encodeToString("Foo".sha256), "1cbec737f863e4922cee63cc2ebbfaafcd1cff8b790d8cfd2e6a5d550b648afa")
    }

    @Test
    fun getSha512() {
        assertEquals(
            Hex.encodeToString("Foo".sha512),
            "4abcd2639957cb23e33f63d70659b602a5923fafcfd2768ef79b0badea637e5c837161aa101a557a1d4deacbd912189e2bb11bf3c0c0c70ef7797217da7e8207"
        )
    }
}
