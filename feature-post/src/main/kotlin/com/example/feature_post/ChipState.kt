package com.example.feature_post

import androidx.compose.runtime.MutableState

data class ChipState(
    val text: String,
    val isSelected: MutableState<Boolean>
)