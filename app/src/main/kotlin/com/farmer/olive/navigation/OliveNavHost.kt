package com.farmer.olive.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import com.farmer.feature_main.OliveScreens

@Composable
fun OliveNavHost(
    navHostController: NavHostController,
    composeNavigator: ComposeNavigator
) {
    NavHost(navController = navHostController, startDestination = OliveScreens.Main.route) {
        oliveHomeNavigation(
            composeNavigator = composeNavigator,
            navController = navHostController
        )
    }
}