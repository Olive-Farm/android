package com.farmer.olive.ui

import androidx.lifecycle.ViewModel
import com.farmer.home.data.CashBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cashBookRepository: CashBookRepository
) : ViewModel() {
    val oliveScreens = listOf(
        OliveScreens.CashBook,
        OliveScreens.Statistics
    )
}