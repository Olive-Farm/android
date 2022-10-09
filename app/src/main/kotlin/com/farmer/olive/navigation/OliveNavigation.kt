package com.farmer.olive.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import com.farmer.feature_main.MainScreen

fun NavGraphBuilder.oliveHomeNavigation(
    composeNavigator: ComposeNavigator
) {
    composable(route = OliveScreens.Main.route) {
        MainScreen()
    }
}