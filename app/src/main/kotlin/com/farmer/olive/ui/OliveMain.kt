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
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.farmer.olive.navigation.OliveNavHost
import com.farmer.olive.ui.theme.Green500
import com.farmer.olive.ui.theme.OliveTheme

@Composable
fun OliveMain(
    composeNavigator: ComposeNavigator,
    viewModel: MainViewModel = hiltViewModel()
) {
    OliveTheme {
        val navHostController = rememberNavController()
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
                    onClick = { /*TODO*/ }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            },
            isFloatingActionButtonDocked = true
        ) {
            OliveNavHost(navHostController = navHostController, composeNavigator = composeNavigator)
        }
    }
}