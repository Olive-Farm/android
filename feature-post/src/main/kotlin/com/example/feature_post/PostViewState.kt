package com.example.feature_post

data class PostViewState(
    var isSpendState: Boolean = true,
    val needNameState: Boolean = false,
    val needAmountState: Boolean = false,
    val needDateState: Boolean = false,
    val dismissDialogState: Boolean = false,
    val isLoading: Boolean = false,
    val loadingText: String = "영수증을 보는 중",
    //편집모드 판별
    val isEditState: Boolean = false
)