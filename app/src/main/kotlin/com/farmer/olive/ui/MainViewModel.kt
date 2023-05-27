package com.farmer.olive.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.data.network.model.Image
import com.farmer.data.network.model.ImageRequest
import com.farmer.data.network.model.ImageType
import com.farmer.data.repository.OliveRepository
import com.farmer.home.data.CashBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cashBookRepository: CashBookRepository,
    private val oliveRepository: OliveRepository
) : ViewModel() {
    val oliveScreens = listOf(
        OliveScreens.CashBook,
        OliveScreens.Statistics
    )

    init {
//        requestOcr()
    }

    private val _postDialogState =
        MutableStateFlow<PostDialogState>(PostDialogState.NotShowingDialog)
    val postDialogState: StateFlow<PostDialogState> get() = _postDialogState.asStateFlow()

    fun setShowPostDialog(shouldShow: Boolean) {
        _postDialogState.value =
            if (shouldShow) PostDialogState.ShowDialog
            else PostDialogState.NotShowingDialog
    }

//    private fun requestOcr() {
//        viewModelScope.launch {
//            val response = oliveRepository.getReceiptInformation(
//                ImageRequest(
//                    version = "V2",
//                    requestId = "string",
//                    timestamp = 0,
//                    images = Image(
//                        format = "jpg",//ImageType.JPG,
//                        name = "test 1",
//                        data = image
//                    )
//                )
//            )
//            Log.e("@@@response", "response : $response")
//        }
//    }
}

sealed interface PostDialogState {
    object ShowDialog : PostDialogState
    object NotShowingDialog : PostDialogState
}