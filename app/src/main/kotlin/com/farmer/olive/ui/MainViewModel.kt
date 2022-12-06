package com.farmer.olive.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.home.data.CashBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cashBookRepository: CashBookRepository
) : ViewModel() {
    val oliveScreens = listOf(
        OliveScreens.CashBook,
        OliveScreens.Statistics
    )

    val showDialog = mutableStateOf(false)

    fun sendInputCashData(
        time: String,
        name: String,
        amount: String
    ) {
        viewModelScope.launch {
            cashBookRepository.addCashData(time, name, amount.toIntOrNull() ?: -1)
        }
    }
}