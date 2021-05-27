package com.peerbridge.android.ui.context

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.peerbridge.android.model.AppDatabase

val LocalDatabase = compositionLocalOf<AppDatabase> { error("No App Database found!") }

@Composable
fun ComponentActivity.PeerBridgeDatabaseProvider(content: @Composable() () -> Unit) {
    val database = remember { AppDatabase.getInstance(this) }

    CompositionLocalProvider(LocalDatabase provides database) {
        content()
    }
}

@Composable
fun PreviewDatabaseProvider(content: @Composable() () -> Unit) {
    val context = LocalContext.current

    CompositionLocalProvider(LocalDatabase provides AppDatabase.getInstance(context)) {
        content()
    }
}
