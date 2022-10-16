package com.farmer.home.ui.states

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

sealed interface CalendarUiState {

    data class CalendarState(
        val dateList: List<DateUiInfo>,
        val startDayOfMonth: LocalDate,
        val firstDayOfMonth: DayOfWeek,
        val displayYear: Int,
        val displayMonth: Month
    ) : CalendarUiState

    data class Loading(
        val isLoading: Boolean
    ) : CalendarUiState

    data class Error(
        val errorMessages: String
    ) : CalendarUiState
}