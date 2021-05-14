package com.peerbridge.android.model

import com.peerbridge.android.http.Endpoints
import com.peerbridge.android.http.HttpClient
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal typealias NotificationToken = String

@Serializable
data class Notification(val title: String, val body: String) {
    companion object {
        suspend fun send(
            serverKey: String,
            to: NotificationToken,
            notification: Notification,
            data: ByteArray? = null,
        ) {
            val payload = Json.encodeToString(
                mapOf(
                    "to" to to,
                    "notification" to notification,
                    "data" to data
                )
            )

            HttpClient.post("${Endpoints.fcm}/fcm/send", payload) {
                addHeader("Authorization", "key=$serverKey")
                addHeader("Content-Type", "application/json")
            }
        }
    }
}
