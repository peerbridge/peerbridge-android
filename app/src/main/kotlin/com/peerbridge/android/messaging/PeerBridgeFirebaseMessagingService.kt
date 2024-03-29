package com.peerbridge.android.messaging

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.peerbridge.android.R


class PeerBridgeFirebaseMessagingService : FirebaseMessagingService() {
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        notificationToken = token
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.i(TAG, "Message: $remoteMessage")
    }

    private companion object {
        const val TAG = "MessagingService"
    }
}


var Context.notificationToken: String?
    get() {
        val file = getString(R.string.messaging_preferences_file)
        val key = getString(R.string.messaging_preferences_key)
        val preferences = getSharedPreferences(file, Context.MODE_PRIVATE)
        return preferences.getString(key, null)
    }
    set(value) {
        val file = getString(R.string.messaging_preferences_file)
        val key = getString(R.string.messaging_preferences_key)
        val preferences = getSharedPreferences(file, Context.MODE_PRIVATE)
        preferences.edit(commit = true) { putString(key, value) }
    }
