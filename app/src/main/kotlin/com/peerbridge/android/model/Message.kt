package com.peerbridge.android.model

import androidx.compose.runtime.Immutable
import com.peerbridge.android.crypto.KeyPair
import com.peerbridge.android.crypto.PublicKey
import com.peerbridge.android.data.defaultPublicKey
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

@Immutable
@Serializable
sealed class Message(@Transient var transaction: Transaction? = null) {
    abstract val teaser: String

    val sender: PublicKey
        get() = transaction?.let { PublicKey(it.sender) } ?: defaultPublicKey

    val receiver: PublicKey
        get() = transaction?.let { PublicKey(it.receiver) } ?: defaultPublicKey

    suspend fun send(keyPair: KeyPair, partnerPublicKey: PublicKey): Transaction? {
        val messageData = Json.encodeToString(this).toByteArray(Charsets.UTF_8)
        return Transaction.create(keyPair, partnerPublicKey, messageData)
    }

    companion object {
        operator fun invoke(data: ByteArray? = null): Message = data?.let {
            Json.decodeFromString(String(it, Charsets.UTF_8))
        } ?: UnknownMessage()
    }
}

@Immutable
@Serializable
@SerialName("content")
data class ContentMessage(val content: String) : Message() {
    override val teaser: String
        get() = content
}

@Immutable
@Serializable
@SerialName("token")
data class TokenMessage(val token: NotificationToken) : Message() {
    override val teaser: String
        get() = "This user has shared a push notification token."
}

@Immutable
@Serializable
@SerialName("unknown")
class UnknownMessage : Message() {
    override val teaser: String
        get() = "Unknown Message."
}
