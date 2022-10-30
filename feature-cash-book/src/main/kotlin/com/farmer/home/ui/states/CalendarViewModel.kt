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

        val dateList = getDateListOfMonth(
            getInitialDayOfMonth(firstDayOfMonth),
            daysInMonth,
            displayMonth,
            currentYear
        )
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

    fun moveToPreviousMonth() {
        viewModelState.update {
            if (it is CalendarUiState.CalendarState) {
                val prevYear =
                    if (it.displayMonth - 1 == Month.DECEMBER) it.displayYear - 1
                    else it.displayYear
                val prevMonth = it.displayMonth - 1
                val firstDayOfMonth = getFirstDayOfMonth(prevYear, prevMonth)
                val dateList = getDateListOfMonth(
                    initialDayOfMonth = getInitialDayOfMonth(firstDayOfMonth),
                    daysInMonth = prevMonth.minLength(),
                    displayMonth = prevMonth,
                    currentYear = prevYear
                )
                it.copy(
                    dateList = dateList,
                    displayMonth = prevMonth,
                    displayYear = prevYear
                )
            } else {
                CalendarUiState.Error("Connection Error. Please try it again.")
            }
        }
    }

    fun moveToNextMonth() {
        viewModelState.update {
            if (it is CalendarUiState.CalendarState) {
                val nextYear =
                    if (it.displayMonth + 1 == Month.JANUARY) it.displayYear + 1
                    else it.displayYear
                val nextMonth = it.displayMonth + 1
                val firstDayOfMonth = getFirstDayOfMonth(nextYear, nextMonth)
                val dateList = getDateListOfMonth(
                    initialDayOfMonth = getInitialDayOfMonth(firstDayOfMonth),
                    daysInMonth = nextMonth.minLength(),
                    displayMonth = nextMonth,
                    currentYear = nextYear
                )
                it.copy(
                    dateList = dateList,
                    displayMonth = nextMonth,
                    displayYear = nextYear
                )
            } else {
                CalendarUiState.Error("Connection Error. Please try it again.")
            }
        }
    }

    fun setDetailDialogState(
        shouldShow: Boolean,
        clickedDateOfMonth: Int? = null
    ) {
        viewModelState.update { uiState ->
            if (uiState is CalendarUiState.CalendarState) {
                // If dialog is already shown, don't have to calculate date info.
                if (shouldShow) {
                    var clickedDateUiInfo = DateUiInfo.EMPTY
                    val newDateList = buildList {
                        uiState.dateList.forEach { dateInfo ->
                            val isClickedDate = dateInfo.dateOfMonth == clickedDateOfMonth
                            val newDateUiInfo = dateInfo.copy(isClickedDate = isClickedDate)
                            if(isClickedDate) clickedDateUiInfo = newDateUiInfo
                            add(newDateUiInfo)
                        }
                    }
                    uiState.copy(
                        dateList = newDateList,
                        showDetailDialog = !uiState.showDetailDialog,
                        clickedDateInfo = clickedDateUiInfo
                    )
                } else {
                    uiState.copy(
                        showDetailDialog = !uiState.showDetailDialog
                    )
                }
            } else {
                CalendarUiState.Error("Connection Error. Please try it again.")
            }
        }
    }

    private fun getDateListOfMonth(
        initialDayOfMonth: Int,
        daysInMonth: Int,
        displayMonth: Month,
        currentYear: Int
    ): List<DateUiInfo> {
        return buildList {
            (initialDayOfMonth..daysInMonth).forEach { date ->
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

    private fun getFirstDayOfMonth(year: Int, month: Month): DayOfWeek {
        return java.time.LocalDate.of(year, month, 1).dayOfWeek
    }

    private companion object {
        private const val VALUE_TO_MODIFY_START_DATE = 2
    }
}