package com.peerbridge.android.ui.context

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.peerbridge.android.R
import com.peerbridge.android.crypto.KeyPair

val LocalKeyPair = compositionLocalOf { KeyPair.create() }

@Composable
fun ComponentActivity.PeerBridgeKeyPairProvider(content: @Composable() () -> Unit) {
    val keyPair = remember {
        val preferencesKey by lazy { getString(R.string.user_preferences_key) }
        val preferencesFile by lazy { getString(R.string.user_preferences_file) }

        val mainKey by lazy {
            MasterKey.Builder(applicationContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        }

        val sharedPreferences by lazy {
            EncryptedSharedPreferences.create(
                applicationContext,
                preferencesFile,
                mainKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        with(sharedPreferences) {
            getString(preferencesKey, null)?.let { KeyPair.restore(it) } ?: KeyPair.create()
                .apply { edit(commit = true) { putString(preferencesKey, privateKeyHex) } }
        }
    }


    CompositionLocalProvider(LocalKeyPair provides keyPair) {
        content()
    }
}
