package com.farmer.olive.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.farmer.olive.navigation.OliveNavHost
import com.farmer.olive.ui.theme.OliveTheme

@Composable
fun OliveMain(
    composeNavigator: ComposeNavigator
) {
    OliveTheme {
        val navHostController = rememberNavController()
        OliveNavHost(navHostController = navHostController, composeNavigator = composeNavigator)
    }
}