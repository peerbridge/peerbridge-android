package com.peerbridge.android.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.peerbridge.android.R
import com.peerbridge.android.Screen
import com.peerbridge.android.crypto.PublicKey
import com.peerbridge.android.data.SampleMessageProvider
import com.peerbridge.android.data.SampleMessagesProvider
import com.peerbridge.android.model.Message
import com.peerbridge.android.model.asMessage
import com.peerbridge.android.ui.component.Avatar
import com.peerbridge.android.ui.component.PeerBridgeAppBar
import com.peerbridge.android.ui.component.PeerBridgeIcon
import com.peerbridge.android.ui.context.*
import com.peerbridge.android.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Composable
fun ChatItem(message: Message, publicKey: PublicKey, onClick: () -> Unit = {}) {
    Surface(modifier = Modifier
        .clickable(onClick = onClick)
        .padding(horizontal = 8.dp, vertical = 6.dp)) {
        Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Avatar(publicKeyHex = publicKey.value)
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                Row(modifier = Modifier.height(24.dp)) {
                    Text(text = publicKey.short, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.weight(1F))
                    message.transaction?.let {
                        Text(text = it.relativeTime, fontSize = 12.sp, style = MaterialTheme.typography.body2)
                    }
                }
                Row(modifier = Modifier.height(24.dp)) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(text = stringResource(id = R.string.user_joined, publicKey.short), fontStyle = FontStyle.Italic, style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatItemPreview(@PreviewParameter(SampleMessageProvider::class, 2)  params: Pair<Message, Boolean>) {
    val (message, isDarkTheme) = params
    PeerBridgeTheme(darkTheme = isDarkTheme) {
        Surface(color = MaterialTheme.colors.background) {
            ChatItem(message = message, publicKey = message.receiver)
        }
    }
}

@Composable
fun Home(navController: NavHostController, initialMessages: List<Message> = emptyList()) {
    val transactionDao = LocalDatabase.current.transactionDao()
    val keyPair = LocalKeyPair.current

    val messages = transactionDao
        .findContactsByPublicKey(keyPair.publicKeyHex)
        .flatMapLatest { contacts ->
            val contactFlows = contacts.map { transactionDao.findLastByPublicKey(it).asMessage(keyPair) }
            combine(contactFlows, Array<Message>::toList)
        }
        .flowOn(Dispatchers.IO)
        .dropWhile { it.isEmpty() }
        .collectAsState(initial = initialMessages)

    val composableScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        PeerBridgeAppBar {
            Text(text = "PeerBridge", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.weight(1F))
            PeerBridgeIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_refresh),
                contentDescription = stringResource(id = R.string.refresh),
                modifier = Modifier.clickable { composableScope.launch { transactionDao.update(keyPair.publicKeyHex) } },
            )
            PeerBridgeIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                contentDescription = stringResource(id = R.string.add),
                modifier = Modifier.clickable { navController.navigate(Screen.Pair.route) },
                size = 28.dp
            )
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 6.dp)) {
            items(messages.value) {
                val localPublicKey = PublicKey(LocalKeyPair.current.publicKeyHex)
                val partnerPublicKey = if (it.receiver == localPublicKey) it.sender else it.receiver

                ChatItem(
                    message = it,
                    publicKey = partnerPublicKey,
                    onClick = {
                        navController.navigate(Screen.Chat.route.replace("{publicKey}", partnerPublicKey.value))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun HomePreview(@PreviewParameter(SampleMessagesProvider::class) params: Pair<List<Message>, Boolean>) {
    val (messages, isDarkTheme) = params
    PreviewDatabaseProvider {
        PreviewKeyPairProvider {
            PeerBridgeTheme(darkTheme = isDarkTheme) {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    Home(navController = navController, initialMessages = messages)
                }
            }
        }
    }
}
