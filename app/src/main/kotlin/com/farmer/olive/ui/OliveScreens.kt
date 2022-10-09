package com.farmer.olive.ui

// todo screen name should be changed to string id
sealed class OliveScreens(
    val route: String,
    val screenName: String = ""
) {
    // cash book screen
    object CashBook : OliveScreens("cash_book", "Cashbook")

    // statistics screen
    object Statistics : OliveScreens("statistics", "Statistics")
}
