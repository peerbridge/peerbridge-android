package com.peerbridge.android.messaging

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.edit
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PeerBridgeFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var broadcaster: LocalBroadcastManager

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        storeNotificationToken(this, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.i(TAG, "Message: $remoteMessage")

        Handler(Looper.getMainLooper()).post {
            remoteMessage.notification?.let {
                val intent = Intent("MessageNotification")
                broadcaster.sendBroadcast(intent)
            }
        }
    }

    private companion object {
        const val TAG = "MessagingService"

        const val FILE = "com.peerbridge.android.messaging.PeerBridgeFirebaseMessagingService"
        const val KEY = "Token"

        fun storeNotificationToken(context: Context, token: String) = with(context) { getSharedPreferences(FILE, Context.MODE_PRIVATE).edit(commit = true) { putString(KEY, token) } }
    }
}
