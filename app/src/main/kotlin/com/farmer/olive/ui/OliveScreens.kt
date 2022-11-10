package com.farmer.olive.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

// todo screen name should be changed to string id
sealed class OliveScreens(
    val route: String,
    val screenName: String = "",
    val icon: ImageVector
) {
    // cash book screen
    object CashBook : OliveScreens(
        route = "cash_book",
        screenName = "Cashbook",
        icon = Icons.Filled.List
    )

    // statistics screen
    object Statistics : OliveScreens(
        route = "statistics",
        screenName = "Statistics",
        icon = Icons.Filled.ShoppingCart
    )
}
