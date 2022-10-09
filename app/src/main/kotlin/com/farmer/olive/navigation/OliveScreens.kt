package com.farmer.olive.navigation

sealed class OliveScreens(
    val route: String
) {
    // home screen
    object Home : OliveScreens("home")

    // main screen
    object Main : OliveScreens("main")
}
