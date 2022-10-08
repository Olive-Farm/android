package com.farmer.olive.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import com.farmer.home.HomeScreen

fun NavGraphBuilder.oliveHomeNavigation(
    composeNavigator: ComposeNavigator
) {
    composable(route = OliveScreens.Home.route) {
        HomeScreen()
    }
}