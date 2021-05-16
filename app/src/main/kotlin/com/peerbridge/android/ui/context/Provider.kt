package com.peerbridge.android.ui.context

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.peerbridge.android.R
import com.peerbridge.android.crypto.KeyPair

val LocalKeyPair = compositionLocalOf { KeyPair.create() }

@Composable
fun KeyPairProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current

    val keyPair: KeyPair = remember {
        val preferencesKey = context.getString(R.string.user_preferences_key)
        val preferencesFile = context.getString(R.string.user_preferences_file)
        val mainKey = MasterKey.Builder(context.applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            context.applicationContext,
            preferencesFile,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.getString(preferencesKey, null)?.let { KeyPair.restore(it) }
            ?: KeyPair.create().apply {
                sharedPreferences.edit(commit = true) {
                    putString(preferencesKey, privateKeyHex)
                }
            }
    }

    CompositionLocalProvider(LocalKeyPair provides keyPair) {
        content()
    }
}
