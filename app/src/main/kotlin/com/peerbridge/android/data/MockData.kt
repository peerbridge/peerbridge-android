package com.peerbridge.android.data

import com.peerbridge.android.crypto.KeyPair
import com.peerbridge.android.model.ContentMessage
import com.peerbridge.android.model.Message
import com.peerbridge.android.crypto.PublicKey
import com.peerbridge.android.model.Transaction
import com.peerbridge.android.ui.theme.ThemedPreviewParameterProvider

private const val privateKeyHex = "a32da27d8aff2bcfd159e6a61d9fe13da6cf426bf19c7feb2b4e0d0d914d4d06"
private const val publicKeyHex = "0300db96ed8ea9e16350a16a7d01126ce6f00e6917cd4b2e70f838d159f653b510"
internal val keyPair = KeyPair(privateKeyHex, publicKeyHex)

internal val defaultPublicKey = PublicKey("000000000000000000000000000000000000000000000000000000000000000000")

internal val publicKey = PublicKey("0351d4d6ae4c272bfd963a996224e84158be701644b0ea77f454f49c4752123471")

private val transactions = listOf(
    Transaction(
        id = "30e03387c59439d28ca1ceffd7d9da0e8dce36f5e1240111251d9b0ac0593e35",
        sender = "0351d4d6ae4c272bfd963a996224e84158be701644b0ea77f454f49c4752123471",
        receiver = "03bd25c10115c989142cf3c94f2b1fda572336b96814da422c7ac9d8e9c957a722",
        balance = 0,
        timeUnixNano = 1619885620735930880,
        data = "HQSQcTldJdoAIHwv00gzug6kvit8awJQoemJcNQcVvGkDn2SpFivv/CBQnjqszIghAXjSkSLTP+PlREcYSwh",
        fee = 1,
        signature = "bc86986bea7e9fb6be43880276d5108784eea4afda001180e0d23691a9a64c6d1ee1a7118a6adaa7469b9fd6759aead43650d6d24a40b451997f14ec73e0d59a",
        blockID = "e50bb8f11c7600145290b5f0ee12e2c139aa38cc5d2c44bfe8190829a9aaec81"
    ),
    Transaction(
        id = "6198f66697d4a292784c3321cd6afe243019542b606c197e1fedfaba3074e8e2",
        sender = "0351d4d6ae4c272bfd963a996224e84158be701644b0ea77f454f49c4752123471",
        receiver = "03bd25c10115c989142cf3c94f2b1fda572336b96814da422c7ac9d8e9c957a722",
        balance = 0,
        timeUnixNano = 1619885617227615744,
        data = "M9VKFHVkyoz9GdV4mZpLsBc1I9oXnm0Pg18YtAyq3wwkj+VNWGt71RbpmPUak/Z706j9BMxTPNfGHvhqCQs=",
        fee = 1,
        signature = "080419db6444b7dd301e8d58d687ff3898e828e5a5e50f7447c7a8672836bce3050e64601bcea69a84e58295c675fb0cf7a3b1954dad08f6c9cdceb77e1cbc10",
        blockID = "d63cfa432a2559cc6bc6cf06eaa8ac5358c52e7ab4c96861e7d902db5bc20da8"
    ),
    Transaction(
        id = "27638758b1673a68fc78905c7980f52459938092dd85a904c1449e3c7646ff40",
        sender = "03bd25c10115c989142cf3c94f2b1fda572336b96814da422c7ac9d8e9c957a722",
        receiver = "0351d4d6ae4c272bfd963a996224e84158be701644b0ea77f454f49c4752123471",
        balance = 0,
        timeUnixNano = 1619885609466832128,
        data = "MhlotK/uv7uxrZkOOLVwZS1kono73HDcw/Y7EaKV0cW1BQBEdLDqKqBNwNLwIwnc5tVzzp9O2RU7fTn5uhtC5iWt1kSMJmffPMGK",
        fee = 1,
        signature = "6009aa2ec455413f49a650100eb5493c95b4a414aff6e358908b7f9d1809772a3590b8858d63b435d9ea4480add2983c4c112a1bdaf70e958d0c14e2514b1672",
        blockID = "43dce168d329ccf8bb0f5428993cc8a63705d776a7027c5a80ef0033e9ab9020"
    ),
    Transaction(
        id = "81361b717200800d8a1feee2b10795b57134b0d63149e5be57262518000bf228",
        sender = "0351d4d6ae4c272bfd963a996224e84158be701644b0ea77f454f49c4752123471",
        receiver = "03bd25c10115c989142cf3c94f2b1fda572336b96814da422c7ac9d8e9c957a722",
        balance = 0,
        timeUnixNano = System.currentTimeMillis() * 1_000_000L,
        data = "9U95EGD1hOkLxytiZ++JPZNv5ZIu58T4vyJdtd/SdhoG4rIH+WjbVtfPmq1wB5/9M+rYoBqGDOR+Vguz81i80yPiZKP3zCFSfJPTjZWTeRjCzdeMk1hlld0+GfQ=",
        fee = 1,
        signature = "78b7f3bf444e353a840d51c2b4bf50bdaabe3def0e516f653e05a0753c0e2c48091ed497d9b1771693816060cb8c4e9bde099d995a84360eec91b82873186119",
        blockID = "aa6f795fd55b64afa8563cebc073f986e301e7b131a69fd27ae8db98f6b0214d"
    ),
)

private val texts = listOf("Hey :)", "Check it out!", "Thank you!", "Skkrrrt")

private val messages = transactions.zip(texts).map { ContentMessage(it.second).apply { transaction = it.first } }

class SamplePublicKeyProvider: ThemedPreviewParameterProvider<PublicKey>() {
    override val items = sequenceOf(publicKey)
}

class SampleTextProvider: ThemedPreviewParameterProvider<String>() {
    override val items = texts.asSequence()
}

class SampleMessageProvider: ThemedPreviewParameterProvider<Message>() {
    override val items = messages.asSequence()
}

class SampleMessagesProvider: ThemedPreviewParameterProvider<List<Message>>() {
    override val items = sequenceOf(messages)
}
