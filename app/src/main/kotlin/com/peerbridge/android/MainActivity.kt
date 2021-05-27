package com.peerbridge.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.peerbridge.android.crypto.PublicKey
import com.peerbridge.android.data.defaultPublicKey
import com.peerbridge.android.ui.context.LocalKeyPair
import com.peerbridge.android.ui.context.PeerBridgeDatabaseProvider
import com.peerbridge.android.ui.context.PeerBridgeKeyPairProvider
import com.peerbridge.android.ui.theme.PeerBridgeTheme
import com.peerbridge.android.ui.theme.ThemePreviewParameterProvider
import com.peerbridge.android.ui.view.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Pair : Screen("pair")
    object Chat : Screen("chat/{publicKey}")
    object Profile : Screen("profile/{publicKey}")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PeerBridgeKeyPairProvider {
                PeerBridgeDatabaseProvider {
                    PeerBridgeTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(color = MaterialTheme.colors.background) {
                            val navController = rememberNavController()

                            NavHost(
                                navController = navController,
                                startDestination = Screen.Home.route
                            ) {
                                composable(Screen.Home.route) { Home(navController) }
                                composable(Screen.Pair.route) { Pair(navController, ::startActivity) }
                                composable(Screen.Chat.route, deepLinks = listOf(navDeepLink { uriPattern = "peerbridge://pair?publicKey={publicKey}" })) { Chat(navController, it.arguments?.getString("publicKey")?.let { key -> PublicKey(key) } ?: defaultPublicKey) }
                                composable(Screen.Profile.route) { Profile(navController, it.arguments?.getString("publicKey")?.let { key -> PublicKey(key) } ?: defaultPublicKey) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    PeerBridgeTheme(darkTheme = isDarkTheme) {
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            Home(navController = navController)
        }
    }
}
