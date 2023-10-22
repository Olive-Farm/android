package com.farmer.olive.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.farmer.olive.R

// todo screen name should be changed to string id


sealed class OliveScreens(
    val route: String,
    val screenName: String = "",
    val icon: ImageVector
) {
    // cash book screen
    object CashBook : OliveScreens(
        route = "cash_book",
        screenName = "가계부 내역",
        icon = Icons.Filled.EventNote
    )

    // statistics screen
    object Statistics : OliveScreens(
        route = "statistics",
        screenName = "통계",
        icon = Icons.Filled.BarChart
    )
}
