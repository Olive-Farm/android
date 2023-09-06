package com.example.feature_post

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_post.model.UserPostInput
import com.farmer.data.DateInfo
import com.farmer.data.History
import com.farmer.data.network.model.Image
import com.farmer.data.network.model.ImageRequest
import com.farmer.data.repository.OliveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.Instant
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: OliveRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostViewState())
    val uiState: StateFlow<PostViewState> get() = _uiState.asStateFlow()

    fun setChipState(isSpend: Boolean) {
        _uiState.update {
            it.copy(
                isSpendState = isSpend
            )
        }
    }

    fun postCashData(userPostInput: UserPostInput) {
        _uiState.update {
            it.copy(
                needNameState = userPostInput.name.isEmpty(),
                needAmountState = userPostInput.amount.isEmpty(),
                needDateState = userPostInput.year == 0 || userPostInput.month == 0 || userPostInput.date == 0
            )
        }
        if (userPostInput.name.isEmpty() || userPostInput.amount.isEmpty() || (userPostInput.year == 0 || userPostInput.month == 0 || userPostInput.date == 0)) return
        viewModelScope.launch {
            userPostInput.amount.toIntOrNull()?.let { userInputSpendAmount ->
                val spendTransact = when {
                    _uiState.value.isSpendState.not() -> {
                        History.Transact(
                            earnList = listOf(
                                History.Transact.TransactData(
                                    price = userInputSpendAmount,
                                    item = userPostInput.name
                                )
                            )
                        )
                    }
                    _uiState.value.isSpendState -> {
                        History.Transact(
                            spendList = listOf(
                                History.Transact.TransactData(
                                    price = userInputSpendAmount,
                                    item = userPostInput.name
                                )
                            )
                        )
                    }
                    else -> History.Transact()
                }
                val userInputHistory = History(
                    year = userPostInput.year,
                    month = userPostInput.month,
                    date = userPostInput.date,
                    dayOfWeek = "",
                    tool = "", // todo
                    memo = "", // todo
                    spendList = spendTransact
                )
                _uiState.update { it.copy(dismissDialogState = true) }
                repository.insertHistory(userInputHistory)
            }
        }
    }

    fun refreshState() {
        _uiState.update { PostViewState() }
    }

    fun requestOcr(imageInputStream: InputStream?) {
        if (imageInputStream == null) return
        viewModelScope.launch {
            val selectedImage = BitmapFactory.decodeStream(imageInputStream)
            val stream = ByteArrayOutputStream()
            selectedImage.compress(Bitmap.CompressFormat.PNG, 10, stream)
            val image = stream.toByteArray()
            val imageString = Base64.encodeToString(image, 0)
            Log.e("@@@encdedImage", "size : ${imageString.length}")
            val response = repository.getReceiptInformation(
                ImageRequest(
                     version = "V2",
                    requestId = UUID.randomUUID().toString(),
                    timestamp = (Instant.now().toEpochMilli()),
                    images = listOf(
                        Image(
                        format = "png",
                        name = "test 1",
                        data = imageString
                        )
                    )
                )
            )
            Log.e("@@@response", "response : $response")
        }
    }
}
