package com.farmer.feature_main

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator

@Composable
fun MainScreen(
    composeNavigator: ComposeNavigator,
    navController: NavController
) {
    Text("Hello Main Screen!")
}