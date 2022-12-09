package com.farmer.home.ui.states

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.home.data.CashBookRepository
import com.farmer.home.model.response.AllUserData
import com.farmer.home.util.toKotlinDateTimeMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.todayIn
import javax.inject.Inject

@HiltViewModel
class SMSModel @Inject constructor(
    private val repository: CashBookRepository
) : ViewModel() {

    fun sendInputCashDataAndDismiss(
        time: String,
        name: String,
        amount: String
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.addCashData(time, name, amount.toIntOrNull() ?: -1)
            }
        }
    }


}