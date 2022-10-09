package com.farmer.feature_main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    val oliveScreens = listOf(
        OliveScreens.CashBook,
        OliveScreens.Statistics
    )
}