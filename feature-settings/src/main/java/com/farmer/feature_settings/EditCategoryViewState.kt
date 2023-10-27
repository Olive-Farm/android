package com.farmer.feature_settings

data class EditCategoryViewState(
    val dismissDialogState: Boolean = false,
    val addState: Boolean = false,
    val editState: Boolean = false,
    val deleteState: Boolean = false,
    val needNameState: Boolean = false
)
