package com.farmer.olive.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.example.feature_post.PostDialog
import com.farmer.olive.navigation.OliveNavHost
import com.farmer.olive.ui.theme.Green500
import com.farmer.olive.ui.theme.OliveTheme

@Composable
fun OliveMain(
    composeNavigator: ComposeNavigator,
    viewModel: MainViewModel = hiltViewModel(),
) {
    OliveTheme {
        val dialogState = viewModel.postDialogState.collectAsState()
        val navHostController = rememberNavController()
        ShowPostDialogByState(
            dialogState = dialogState,
            onDismissRequest = {
                viewModel.setShowPostDialog(false)
            }
        )

        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                BottomAppBar(
                    cutoutShape = CircleShape,
                    backgroundColor = Color.White
                ) {
                    OliveBottomNavigation(navHostController, viewModel.oliveScreens)
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = Green500,
                    onClick = {
                        viewModel.setShowPostDialog(true)
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            },
            isFloatingActionButtonDocked = true
        ) { _ ->
            OliveNavHost(navHostController = navHostController, composeNavigator = composeNavigator)
        }
    }
}

@Composable
private fun ShowPostDialogByState(
    dialogState: State<PostDialogState>,
    onDismissRequest: () -> Unit
) {
    when (dialogState.value) {
        PostDialogState.ShowDialog -> {
            PostDialog(onDismissRequest = { onDismissRequest() })
        }

        PostDialogState.NotShowingDialog -> Unit
    }
}