package com.farmer.feature_main

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainScreen(
    composeNavigator: ComposeNavigator,
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            viewModel.oliveScreens.forEach {

            }
        }
    ) {
        Text("Hello Main Screen!")
    }
}