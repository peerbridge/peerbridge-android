package com.peerbridge.android.serialization

import android.util.Base64
import com.peerbridge.secp256k1.Hex
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@Serializer(forClass = ByteArray::class)
object ByteArrayBase64Serializer : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ByteArrayBase64", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): ByteArray = Base64.decode(decoder.decodeString(), Base64.DEFAULT)
    override fun serialize(encoder: Encoder, value: ByteArray) = encoder.encodeString(Base64.encodeToString(value, Base64.DEFAULT))
}

@Serializer(forClass = ByteArray::class)
object ByteArrayHexSerializer : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ByteArrayHex", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): ByteArray = Hex.decodeString(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: ByteArray) = encoder.encodeString(Hex.encodeToString(value))
}
