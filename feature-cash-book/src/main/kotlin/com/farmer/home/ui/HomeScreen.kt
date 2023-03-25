package com.farmer.home.ui

import androidx.compose.runtime.Composable

@Composable
fun CashbookScreen(calendarViewModel: TempCalendarViewModel) {
    Calendar(viewmodel = calendarViewModel)
}