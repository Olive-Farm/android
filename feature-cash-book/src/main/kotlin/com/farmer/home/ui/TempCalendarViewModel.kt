package com.farmer.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.data.repository.OliveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TempCalendarViewModel @Inject constructor(
    repository: OliveRepository
) : ViewModel() {
    private val _currentLocalDate =
        MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    val calendarUiState: StateFlow<CalendarUiState> =
        _currentLocalDate.flatMapLatest { currentDate ->
            calendarUiState(repository, currentDate)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CalendarUiState.Loading
        )

    fun moveToNextMonth() {
        _currentLocalDate.value = _currentLocalDate.value.plus(DatePeriod(months = 1))
    }

    fun moveToPreviousMonth() {
        _currentLocalDate.value = _currentLocalDate.value.minus(DatePeriod(months = 1))
    }
}

private fun calendarUiState(
    repository: OliveRepository,
    currentDate: LocalDate
): Flow<CalendarUiState> {
    val monthHistoryList = repository.getDateInfoByMonth(currentDate)
    return monthHistoryList
        .map { dateInfoList ->
            // 달력 첫 부분의 빈 부분 갯수
            val emptyFirstDateCount = dateInfoList.first().date.dayOfWeek.value
            val dateViewInfoList = buildList {
                repeat(emptyFirstDateCount) {
                    add(DateViewInfo(dateInfo = null, isEmptyDate = true))
                }
                dateInfoList.forEach { dateInfo ->
                    add(DateViewInfo(dateInfo = dateInfo, isEmptyDate = false))
                }
            }
            CalendarUiState.Success(dateViewInfoList)
        }
}

sealed interface CalendarUiState {
    data class Success(val dateViewInfo: List<DateViewInfo>) : CalendarUiState
    object Error : CalendarUiState
    object Loading : CalendarUiState
}
