package com.peerbridge.android.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.peerbridge.android.data.SampleMessageProvider
import com.peerbridge.android.data.SampleMessagesProvider
import com.peerbridge.android.data.publicKey
import com.peerbridge.android.model.*
import com.peerbridge.android.ui.component.Avatar
import com.peerbridge.android.ui.component.PeerBridgeAppBar
import com.peerbridge.android.ui.theme.*

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
fun Chat(navController: NavHostController, messages: List<Message> = emptyList()) {
    // TODO: find partner public key by comparing to user public key
    val partnerPublicKey = messages[0].receiver

    Column(modifier = Modifier.fillMaxSize()) {
        PeerBridgeAppBar {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(id = R.string.add),
                modifier = Modifier
                    .clickable { navController.navigateUp() }
                    .padding(horizontal = 6.dp)
                    .width(24.dp)
                    .height(24.dp),
                tint = MaterialTheme.colors.onSurface
            )
            Text(text = partnerPublicKey.short, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.weight(1F))
            Avatar(publicKeyHex = partnerPublicKey.value, size = 24.dp)
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)) {
            itemsIndexed(messages) { index, message ->
                val prevAuthor = messages.getOrNull(index - 1)?.sender
                val nextAuthor = messages.getOrNull(index + 1)?.sender
                val isFirstMessageByAuthor = prevAuthor != message.sender
                val isLastMessageByAuthor = nextAuthor != message.sender

                // TODO: check if sender is current user
                val isMe = message.sender == publicKey
                val horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start

                if (isFirstMessageByAuthor && index > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = horizontalArrangement) {
                    ChatBubble(
                        message = message,
                        modifier = Modifier.padding(2.dp),
                        alignEnd = isMe,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun ChatPreview(@PreviewParameter(SampleMessagesProvider::class) params: Pair<List<Message>, Boolean>) {
    val (state, isDarkTheme) = params
    PeerBridgeTheme(darkTheme = isDarkTheme) {
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            Chat(navController, state)
        }
    }
}
