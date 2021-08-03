package com.peerbridge.android.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.peerbridge.android.R
import com.peerbridge.android.crypto.PublicKey
import com.peerbridge.android.data.SampleMessageProvider
import com.peerbridge.android.data.SampleMessagesProvider
import com.peerbridge.android.messaging.notificationToken
import com.peerbridge.android.model.*
import com.peerbridge.android.ui.component.Avatar
import com.peerbridge.android.ui.component.PeerBridgeAppBar
import com.peerbridge.android.ui.context.LocalDatabase
import com.peerbridge.android.ui.context.LocalKeyPair
import com.peerbridge.android.ui.context.PreviewDatabaseProvider
import com.peerbridge.android.ui.context.PreviewKeyPairProvider
import com.peerbridge.android.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@Composable
fun ChatAction(text: String, modifier: Modifier = Modifier, initialOpen: Boolean = false, content: @Composable () -> Unit = {}) {
    var isOpen by remember { mutableStateOf(initialOpen) }

    val background = if (MaterialTheme.colors.isLight) Grey100 else Grey850
    val iconResource = if (isOpen) R.drawable.ic_chevron_down else R.drawable.ic_chevron_right

    Card(modifier = modifier.clickable { isOpen = true }, shape = MaterialTheme.shapes.large, backgroundColor = background) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = text, modifier = Modifier.weight(1F))
                Icon(
                    imageVector = ImageVector.vectorResource(id = iconResource),
                    contentDescription = stringResource(id = R.string.share),
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .width(24.dp)
                        .height(24.dp),
                    tint = MaterialTheme.colors.onSurface
                )
            }
            if (isOpen) {
                Column(modifier = Modifier.padding(top = 12.dp, end = 36.dp)) {
                    content()
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun ChatActionPreview(@PreviewParameter(ThemePreviewParameterProvider::class)  isDarkTheme: Boolean) {
//    PeerBridgeTheme(darkTheme = isDarkTheme) {
//        Surface(color = MaterialTheme.colors.background) {
//            ChatAction(text = stringResource(id = R.string.user_token_share), modifier = Modifier.padding(12.dp))
//        }
//    }
//}
//@Preview
//@Composable
//fun ChatActionOpenPreview(@PreviewParameter(ThemePreviewParameterProvider::class)  isDarkTheme: Boolean) {
//    PeerBridgeTheme(darkTheme = isDarkTheme) {
//        Surface(color = MaterialTheme.colors.background) {
//            ChatAction(text = stringResource(id = R.string.user_token_share), modifier = Modifier.padding(12.dp), initialOpen = true) {
//                Column(modifier = Modifier.padding(top = 12.dp, end = 36.dp)) {
//                    Text(text = stringResource(id = R.string.user_token_share_description), style = MaterialTheme.typography.body2)
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Button(onClick = { }, shape = MaterialTheme.shapes.large) {
//                        Icon(
//                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_send),
//                            contentDescription = stringResource(id = R.string.share),
//                            modifier = Modifier
//                                .padding(end = 8.dp)
//                                .width(18.dp)
//                                .height(18.dp),
//                            tint = Color.White
//                        )
//                        Text(text = stringResource(id = R.string.user_token_share_action), color = Color.White)
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun ChatBubble(
    message: Message,
    modifier: Modifier = Modifier,
    alignEnd: Boolean = false,
    isFirstMessageByAuthor: Boolean = true,
    isLastMessageByAuthor: Boolean = true,
) {
    val background = if (MaterialTheme.colors.isLight) Grey300 else Grey850
    val color: Color = if (alignEnd) Color.White else contentColorFor(background)

    val horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start
    val shapeCornerSize = CornerSize(2.dp)
    val shape = when {
        alignEnd && isFirstMessageByAuthor && isLastMessageByAuthor -> MaterialTheme.shapes.large.copy(topEnd = shapeCornerSize)
        alignEnd && isFirstMessageByAuthor && !isLastMessageByAuthor -> MaterialTheme.shapes.large.copy(bottomEnd = shapeCornerSize)
        alignEnd && !isFirstMessageByAuthor && isLastMessageByAuthor -> MaterialTheme.shapes.large.copy(topEnd = shapeCornerSize)
        alignEnd && !isFirstMessageByAuthor && !isLastMessageByAuthor -> MaterialTheme.shapes.large.copy(topEnd = shapeCornerSize, bottomEnd = shapeCornerSize)
        !alignEnd && isFirstMessageByAuthor && isLastMessageByAuthor -> MaterialTheme.shapes.large.copy(topStart = shapeCornerSize)
        !alignEnd && isFirstMessageByAuthor && !isLastMessageByAuthor -> MaterialTheme.shapes.large.copy(bottomStart = shapeCornerSize)
        !alignEnd && !isFirstMessageByAuthor && isLastMessageByAuthor -> MaterialTheme.shapes.large.copy(topStart = shapeCornerSize)
        !alignEnd && !isFirstMessageByAuthor && !isLastMessageByAuthor -> MaterialTheme.shapes.large.copy(topStart = shapeCornerSize, bottomStart = shapeCornerSize)
        else -> MaterialTheme.shapes.large
    }

    val elevation: Dp = 1.dp
    val elevationPx = with(LocalDensity.current) { elevation.toPx() }
    val absoluteElevation = LocalAbsoluteElevation.current + elevation

    val text = when(message) {
        is ContentMessage -> message.content
        is TokenMessage -> stringResource(id = R.string.user_token)
        is UnknownMessage -> stringResource(id = R.string.message_unknown)
    }

    CompositionLocalProvider(
        LocalContentColor provides color,
        LocalAbsoluteElevation provides absoluteElevation
    ) {
        Box(
            modifier.graphicsLayer(shadowElevation = elevationPx, shape = shape)
                then(if (alignEnd) {
                    Modifier.background(
                        brush = blueGradient,
                        shape = shape
                    )
                } else {
                    Modifier.background(
                        color = background,
                        shape = shape
                    )
                })
                .clip(shape),
            propagateMinConstraints = true
        ) {
            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = horizontalAlignment) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1
                )
                message.transaction?.let {
                    Row(modifier = Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = it.relativeTime,
                            style = MaterialTheme.typography.caption
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_lock),
                            contentDescription = stringResource(id = R.string.lock),
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .width(10.dp)
                                .height(10.dp),
                            tint = LocalContentColor.current
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatBubblePreview(@PreviewParameter(SampleMessageProvider::class, 2)  params: Pair<Message, Boolean>) {
    val (message, isDarkTheme) = params
    PeerBridgeTheme(darkTheme = isDarkTheme) {
        Surface(color = MaterialTheme.colors.background) {
            ChatBubble(message = message, modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
fun Chat(navController: NavHostController, publicKey: PublicKey, initialMessages: List<Message> = emptyList()) {
    val notificationToken = LocalContext.current.notificationToken
    val transactionDao = LocalDatabase.current.transactionDao()
    val keyPair = LocalKeyPair.current

    val messages = transactionDao
        .findByPublicKey(publicKey.value)
        .asMessages(keyPair)
        .flowOn(Dispatchers.IO)
        .collectAsState(initial = initialMessages)

    val hasSharedToken = messages.value.filterIsInstance<TokenMessage>().any { it.receiver == publicKey }
    val hasPartnerToken = messages.value.filterIsInstance<TokenMessage>().any { it.sender == publicKey }

    val composableScope = rememberCoroutineScope()

    val shareToken = {
        notificationToken?.let { token ->
            composableScope.launch {
                TokenMessage(token).send(keyPair, publicKey)?.let { transaction ->
                    transactionDao.insert(transaction)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PeerBridgeAppBar {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(id = R.string.back),
                modifier = Modifier
                    .clickable { navController.navigateUp() }
                    .padding(horizontal = 6.dp)
                    .width(24.dp)
                    .height(24.dp),
                tint = MaterialTheme.colors.onSurface
            )
            Text(text = publicKey.short, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.weight(1F))
            Avatar(publicKeyHex = publicKey.value, size = 24.dp)
        }
        LazyColumn(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(8.dp)) {
            itemsIndexed(messages.value) { index, message ->
                val prevAuthor = messages.value.getOrNull(index - 1)?.sender
                val nextAuthor = messages.value.getOrNull(index + 1)?.sender
                val isFirstMessageByAuthor = prevAuthor != message.sender
                val isLastMessageByAuthor = nextAuthor != message.sender

                val isFromMe = message.receiver == publicKey // sender == self.publicKey
                val horizontalArrangement = if (isFromMe) Arrangement.End else Arrangement.Start

                if (isFirstMessageByAuthor && index > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = horizontalArrangement) {
                    ChatBubble(
                        message = message,
                        modifier = Modifier.padding(2.dp),
                        alignEnd = isFromMe,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }
            }
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            if(!hasSharedToken) {
                ChatAction(text = stringResource(id = R.string.user_token_share), modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(text = stringResource(id = R.string.user_token_share_description), style = MaterialTheme.typography.body2)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { shareToken() }, shape = MaterialTheme.shapes.large) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_send),
                            contentDescription = stringResource(id = R.string.share),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .width(18.dp)
                                .height(18.dp),
                            tint = Color.White
                        )
                        Text(text = stringResource(id = R.string.user_token_share_action), color = Color.White)
                    }
                }
            }
            if (!hasPartnerToken) {
                ChatAction(text = stringResource(id = R.string.partner_token_share), modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(text = stringResource(id = R.string.partner_token_share_description), style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatPreview(@PreviewParameter(SampleMessagesProvider::class) params: Pair<List<Message>, Boolean>) {
    val (messages, isDarkTheme) = params
    val publicKey = messages[0].receiver
    PreviewDatabaseProvider {
        PreviewKeyPairProvider {
            PeerBridgeTheme(darkTheme = isDarkTheme) {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    Chat(navController, publicKey, messages)
                }
            }
        }
    }
}
