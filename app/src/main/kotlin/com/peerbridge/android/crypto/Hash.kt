package com.peerbridge.android.crypto

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Computes a cryptographic hash of a String using the MD5 Algorithm
 */
val String.md5: ByteArray
    get() = hash(this, "MD5")

/**
 * Computes a cryptographic hash of a String using the SHA-1 Algorithm
 */
val String.sha1: ByteArray
    get() = hash(this, "SHA-1")

/**
 * Computes a cryptographic hash of a String using the SHA-256 Algorithm
 */
val String.sha256: ByteArray
    get() = hash(this, "SHA-256")

/**
 * Computes a cryptographic hash of a String using the SHA-512 Algorithm
 */
val String.sha512: ByteArray
    get() = hash(this, "SHA-512")

/**
 * Computes a cryptographic hash of an [input] using the provided [algorithm]
 * Supported algorithms on Android:
 *
 * Algorithm	Supported API Levels
 * MD5          1+
 * SHA-1	    1+
 * SHA-224	    1-8,22+
 * SHA-256	    1+
 * SHA-384	    1+
 * SHA-512	    1+
 *
 * @param input the input the hash
 * @param algorithm the algorithm to use for hashing
 * @throws NoSuchAlgorithmException if no Provider supports the specified [algorithm].
 * @return the cryptographic hash
 */
@Throws(NoSuchAlgorithmException::class)
private fun hash(input: String, algorithm: String): ByteArray =
    MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
