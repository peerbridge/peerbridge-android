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
import androidx.navigation.compose.rememberNavController
import com.peerbridge.android.ui.context.PeerBridgeRoot
import com.peerbridge.android.ui.theme.PeerBridgeTheme
import com.peerbridge.android.ui.theme.ThemePreviewParameterProvider
import com.peerbridge.android.ui.view.Home
import com.peerbridge.android.ui.view.Pair

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Pair : Screen("pair")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PeerBridgeRoot {
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
                            composable(Screen.Chat.route) { Home(navController) }
                            composable(Screen.Profile.route) { Home(navController) }
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
