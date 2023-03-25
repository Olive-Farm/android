package com.farmer.olive.navigation

import android.util.Log
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
    composeNavigator: ComposeNavigator,
    calendarViewModel: TempCalendarViewModel = hiltViewModel()
) {
    NavHost(navController = navHostController, startDestination = OliveScreens.CashBook.route) {
        composable(route = "cash_book") {
            Log.e("@@@navBackStackEntry", "it : ${it}")
            Log.e("@@@navBackStackEntry", "it : ${it.arguments}")
            Log.e("@@@navBackStackEntry", "it : ${it.destination}")
            Log.e("@@@navBackStackEntry", "it : ${it.id}")
            Log.e("@@@navBackStackEntry", "it : ${it.maxLifecycle}")
            Log.e("@@@navBackStackEntry", "it : ${it.savedStateHandle}")
            CashbookScreen(calendarViewModel)
        }
        composable(route = "statistics") {
            StatisticsScreen()
        }
    }
}