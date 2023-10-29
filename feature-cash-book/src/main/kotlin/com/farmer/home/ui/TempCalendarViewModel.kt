package com.farmer.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.data.DateInfo
import com.farmer.data.repository.OliveRepository
import com.farmer.navigator.SettingsActivityNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
    repository: OliveRepository,
    val settingsActivityNavigator: SettingsActivityNavigator
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _dialogUiState = MutableStateFlow<DialogUiState>(DialogUiState.NotShowing)
    val dialogUiState: StateFlow<DialogUiState> get() = _dialogUiState.asStateFlow()

    private val _editCategoryDialogState =
        MutableStateFlow<EditCategoryDialogState>(EditCategoryDialogState.NotShowingEditDialog)

    val editCategoryDialogState: StateFlow<EditCategoryDialogState> get() = _editCategoryDialogState.asStateFlow()


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

    fun refreshMonth() {
        _isRefreshing.update { true }
        // 강제 emit하기 위해 아래처럼 넣었는데 더 좋은 방법 없을지 고민해봐야 함.
        _currentLocalDate.update {
            val newDate = _currentLocalDate.value.minus(DatePeriod(days = 1))
            if (newDate.month == it.month) newDate
            else _currentLocalDate.value.plus(DatePeriod(days = 1))
        }
        _isRefreshing.update { false }
    }

    fun setShowDetailDialog(shouldShow: Boolean, clickedDateInfo: DateInfo?) {
        _dialogUiState.value =
            if (shouldShow) DialogUiState.DetailDialog(clickedDateInfo)
            else DialogUiState.NotShowing
    }

    fun setShowPostDialog(shouldShow: Boolean) {
        _dialogUiState.value =
            if (shouldShow) DialogUiState.PostDialog
            else DialogUiState.NotShowing
    }

    fun setShowEditCategoryDialog(shouldShow: Boolean) {
        _dialogUiState.value =
            if (shouldShow) DialogUiState.EditCategoryDialog
            else DialogUiState.NotShowing
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
            var totalSpend = 0
            var totalIncome = 0
            val dateViewInfoList = buildList {
                repeat(emptyFirstDateCount) {
                    add(DateViewInfo(dateInfo = null, isEmptyDate = true))
                }
                dateInfoList.forEach { dateInfo ->
                    totalSpend += dateInfo.history?.spendList?.spendList?.sumOf { it.price } ?: 0
                    totalIncome += dateInfo.history?.spendList?.earnList?.sumOf { it.price } ?: 0
                    add(DateViewInfo(dateInfo = dateInfo, isEmptyDate = false))
                }
            }
            CalendarUiState.Success(dateViewInfoList, totalSpend, totalIncome)
        }
}

sealed interface CalendarUiState {
    data class Success(
        val dateViewInfo: List<DateViewInfo>,
        val totalIncome: Int = 0,
        val totalSpend: Int = 0
    ) : CalendarUiState

    object Error : CalendarUiState
    object Loading : CalendarUiState
}

sealed interface DialogUiState {
    data class DetailDialog(val clickedDateInfo: DateInfo?) : DialogUiState

    object PostDialog : DialogUiState

    object NotShowing : DialogUiState

    object EditCategoryDialog : DialogUiState

}

sealed interface EditCategoryDialogState {
    object ShowEditDialog : EditCategoryDialogState

    object NotShowingEditDialog : EditCategoryDialogState
}
