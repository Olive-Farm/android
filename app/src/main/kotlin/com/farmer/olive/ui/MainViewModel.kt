package com.farmer.olive.ui

import androidx.lifecycle.ViewModel
import com.farmer.home.data.CashBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cashBookRepository: CashBookRepository
) : ViewModel() {
    val oliveScreens = listOf(
        OliveScreens.CashBook,
        OliveScreens.Statistics
    )

    private val _postDialogState =
        MutableStateFlow<PostDialogState>(PostDialogState.NotShowingDialog)
    val postDialogState: StateFlow<PostDialogState> get() = _postDialogState.asStateFlow()

    fun setShowPostDialog(shouldShow: Boolean) {
        _postDialogState.value =
            if (shouldShow) PostDialogState.ShowDialog
            else PostDialogState.NotShowingDialog
    }
}

sealed interface PostDialogState {
    object ShowDialog : PostDialogState
    object NotShowingDialog : PostDialogState
}