package com.farmer.olive.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.farmer.olive.navigation.OliveNavHost
import com.farmer.olive.ui.theme.OliveTheme

@Composable
fun OliveMain(
    composeNavigator: ComposeNavigator,
    viewModel: MainViewModel = hiltViewModel()
) {
    OliveTheme {
        val navHostController = rememberNavController()
        Scaffold(
            bottomBar = {
                OliveBottomNavigation(navHostController, viewModel.oliveScreens)
            }
        ) {
            OliveNavHost(navHostController = navHostController, composeNavigator = composeNavigator)
        }
    }
}