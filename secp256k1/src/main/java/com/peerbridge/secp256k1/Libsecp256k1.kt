package com.peerbridge.secp256k1

object Libsecp256k1 {
    /**
     * Used to load the 'secp256k1-android' library on application startup.
     */
    init {
        System.loadLibrary("secp256k1-android")
    }

    /**
     * Generate a new random ECDSA key pair as a pair of private and public key.
     *
     * @return a new random ECDSA key pair in the form [private, public]
     */
    @JvmStatic
    external fun createKeyPair(): Pair<ByteArray, ByteArray>

    /**
     * Generate a new random ECDSA key pair as a pair of private and public key.
     *
     * @return a new random ECDSA key pair in the form [private, public] encoded as hexadecimal strings
     */
    @JvmStatic
    external fun createKeyPairHex(): Pair<String, String>

    /**
     * Compute the public key from a private key
     *
     * @param key - the private key to compute the public key for
     * @return the public key encoded according to the given length
     *         or null if the procedure failed
     */
    @JvmStatic
    external fun computePublicKey(key: ByteArray): ByteArray?

    /**
     * Compute the public key from a private key
     *
     * @param key - the private key to compute the public key for encoded as a hexadecimal string
     * @return the public key encoded encoded as a hexadecimal string according to the given length
     *         or null if the procedure failed
     */
    @JvmStatic
    external fun computePublicKeyHex(key: String): String?

    /**
     * Compute an EC Diffie-Hellman secret in constant time
     *
     * @param privateKey - the private key, which is used as a 32-byte scalar with which to multiply the point
     * @param publicKey - the public key, to compute the secret with
     * @return a 32-byte byte array containing the ECDH secret computed from the point and scalar
     */
    @JvmStatic
    external fun computeSecret(privateKey: ByteArray, publicKey: ByteArray): ByteArray?

    /**
     * Compute an EC Diffie-Hellman secret in constant time
     *
     * @param privateKey - the private key, which is used as a 32-byte scalar with which to
     *                     multiply the point, encoded as a hexadecimal string
     * @param publicKey - the public key, to compute the secret with, encoded as a hexadecimal string
     * @return a hexadecimal encoded 32-byte byte array containing the ECDH secret computed
     *         from the point and scalar
     */
    @JvmStatic
    external fun computeSecretHex(privateKey: String, publicKey: String): String?

    /**
     * Sign creates a recoverable ECDSA signature.
     * The produced signature is in the 65-byte [R || S || V] format where V is 0 or 1.
     *
     * The caller is responsible for ensuring that msg cannot be chosen
     * directly by an attacker. It is usually preferable to use a cryptographic
     * hash function on any input before handing it to this function.
     *
     * @param privateKey - the private key of the signer
     * @param message - the message to sign
     * @return the signature or null if the procedure failed
     */
    @JvmStatic
    external fun sign(privateKey: ByteArray, message: ByteArray): ByteArray?

    /**
     * Sign creates a recoverable ECDSA signature.
     * The produced signature is in the 65-byte [R || S || V] format where V is 0 or 1.
     *
     * The caller is responsible for ensuring that msg cannot be chosen
     * directly by an attacker. It is usually preferable to use a cryptographic
     * hash function on any input before handing it to this function.
     *
     * @param privateKey - the private key of the signer, encoded as a hexadecimal string
     * @param message - the message to sign, encoded as a hexadecimal string
     * @return the signature encoded as a hexadecimal string or null if the procedure failed
     */
    @JvmStatic
    external fun signHex(privateKey: String, message: String): String?

    /**
     * VerifySignature checks that the given pubkey created signature over message.
     * The signature should be in [R || S] format.
     *
     * @param publicKey - the public key of the signer
     * @param message - the message that was signed
     * @param signature - the signature that was created on the message
     * @return a Boolean indicating if the signature is valid
     */
    @JvmStatic
    external fun verifySignature(
        publicKey: ByteArray,
        message: ByteArray,
        signature: ByteArray
    ): Boolean

    /**
     * VerifySignature checks that the given pubkey created signature over message.
     * The signature should be in [R || S] format.
     *
     * @param publicKey - the public key of the signer, encoded as a hexadecimal string
     * @param message - the message that was signed, encoded as a hexadecimal string
     * @param signature - the signature that was created on the message, encoded as a hexadecimal string
     * @return a Boolean indicating if the signature is valid
     */
    @JvmStatic
    external fun verifySignatureHex(
        publicKey: String,
        message: String,
        signature: String
    ): Boolean
}
