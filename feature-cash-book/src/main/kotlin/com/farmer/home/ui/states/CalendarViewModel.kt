package com.farmer.home.ui.states

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.todayIn
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val viewModelState: MutableStateFlow<CalendarUiState> =
        MutableStateFlow(CalendarUiState.Loading(isLoading = true))

    // UI state exposed to the UI
    val uiState: StateFlow<CalendarUiState> = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )

    init {
        val currentDay = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val displayMonth = currentDay.month
        val displayYear = currentDay.year
        val (currentMonth, currentYear) = displayMonth.value to currentDay.year
        val monthStartsWithZero =
            if (currentMonth.toString().length == 1) "0$currentMonth"
            else currentMonth.toString()

        val startDayOfMonth = "${currentDay.year}-$monthStartsWithZero-01".toLocalDate()
        val firstDayOfMonth = startDayOfMonth.dayOfWeek

        val daysInMonth = displayMonth.minLength()

        val dateList = buildList {
            (getInitialDayOfMonth(firstDayOfMonth)..daysInMonth).forEach { date ->
                if (date > 0) {
                    val day = getGeneratedDay(date, displayMonth, currentYear)
                    add(
                        DateUiInfo(
                            dateOfMonth = day.dayOfMonth,
                            sumOfIncome = (-50000..50000).random(),
                            sumOfSpend = (-50000..50000).random(),
                            incomeList = listOf(3000, 20000, 10000),
                            spendList = listOf(20000, 4000, 1000),
                            isClickedDate = false
                        )
                    )
                } else {
                    // Means date is not existing at the day of month.
                    add(DateUiInfo.EMPTY)
                }
            }
        }

        viewModelState.update {
            CalendarUiState.CalendarState(
                dateList = dateList,
                displayMonth = displayMonth,
                displayYear = displayYear,
                firstDayOfMonth = firstDayOfMonth,
                startDayOfMonth = startDayOfMonth
            )
        }
    }

    /**
     * @param firstDayOfMonth First day of month as int. (Monday = 1, Saturday = 6)
     *
     * @return It returns day int of starting day of month. If it starts from monday it returns 1,
     * if the month starts with thursday it returns 4.
     */
    private fun getInitialDayOfMonth(
        firstDayOfMonth: DayOfWeek
    ): Int = -(firstDayOfMonth.value).minus(VALUE_TO_MODIFY_START_DATE)

    /**
     * @return Local date with day, currentMonth and currentYear
     */
    private fun getGeneratedDay(day: Int, currentMonth: Month, currentYear: Int): LocalDate {
        val monthValue =
            if (currentMonth.value.toString().length == 1) "0${currentMonth.value}"
            else currentMonth.value.toString()
        val newDay = if (day.toString().length == 1) "0$day" else day
        return "$currentYear-$monthValue-$newDay".toLocalDate()
    }

    private companion object {
        private const val VALUE_TO_MODIFY_START_DATE = 2
    }
}