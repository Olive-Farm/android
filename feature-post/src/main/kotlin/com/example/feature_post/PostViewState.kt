package com.example.feature_post

data class PostViewState(
    val isSpendState: Boolean = true,
    val needNameState: Boolean = false,
    val needAmountState: Boolean = false,
    val needDateState: Boolean = false,
    val dismissDialogState: Boolean = false,
    val isLoading: Boolean = false
)