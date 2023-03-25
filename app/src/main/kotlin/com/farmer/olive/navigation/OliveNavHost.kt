package com.farmer.olive.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.farmer.olive.ui.OliveScreens
import com.farmer.feature_statistics.StatisticsScreen
import com.farmer.home.ui.CashbookScreen
import com.farmer.home.ui.TempCalendarViewModel

@Composable
fun OliveNavHost(
    navHostController: NavHostController,
    composeNavigator: ComposeNavigator
) {
    NavHost(navController = navHostController, startDestination = OliveScreens.CashBook.route) {
        composable(route = "cash_book") {
            CashbookScreen()
        }
        composable(route = "statistics") {
            StatisticsScreen()
        }
    }
}