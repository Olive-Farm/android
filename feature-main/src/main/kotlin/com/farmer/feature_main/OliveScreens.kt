package com.farmer.feature_main

sealed class OliveScreens(
    val route: String
) {
    // home screen
    object Home : OliveScreens("home")

    // main screen
    object Main : OliveScreens("main")

    // cash book screen
    object CashBook : OliveScreens("cash_book")

    // statistics screen
    object Statistics : OliveScreens("statistics")
}
