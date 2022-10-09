package com.farmer.olive.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import com.farmer.feature_main.MainScreen
import com.farmer.feature_main.OliveScreens

fun NavGraphBuilder.oliveHomeNavigation(
    composeNavigator: ComposeNavigator,
    navController: NavController
) {
    composable(route = OliveScreens.Main.route) {
        MainScreen(composeNavigator, navController)
    }
}