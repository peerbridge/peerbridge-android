package com.peerbridge.android.ui.context

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.room.Room
import com.peerbridge.android.model.AppDatabase

val LocalDatabase = compositionLocalOf<AppDatabase> { error("No App Database found!") }

@Composable
fun ComponentActivity.PeerBridgeDatabaseProvider(content: @Composable() () -> Unit) {
    val database = remember {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "peerbridge")
            .build()
    }

    CompositionLocalProvider(LocalDatabase provides database) {
        content()
    }
}
