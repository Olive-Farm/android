package com.farmer.feature_main

// todo screen name should be changed to string id
sealed class OliveScreens(
    val route: String,
    val screenName: String = ""
) {
    // home screen
    object Home : OliveScreens("home")

    // main screen
    object Main : OliveScreens("main")

    // cash book screen
    object CashBook : OliveScreens("cash_book", "Cashbook")

    // statistics screen
    object Statistics : OliveScreens("statistics", "Statistics")
}
