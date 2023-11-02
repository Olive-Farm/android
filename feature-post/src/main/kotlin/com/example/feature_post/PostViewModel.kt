package com.example.feature_post

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Base64
import android.util.Log
import com.farmer.data.Category
import com.farmer.data.History
import com.farmer.data.network.model.Image
import com.farmer.data.network.model.ImageRequest
import com.farmer.data.repository.OliveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: OliveRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostViewState())
    val uiState: StateFlow<PostViewState> get() = _uiState.asStateFlow()

    val todayDate = LocalDate.now().toString().split("-")

    val name = mutableStateOf(TextFieldValue(""))
    val amount = mutableStateOf(TextFieldValue(""))
    val category = mutableStateOf(String())
    val yearState = mutableStateOf(todayDate[0].toInt())
    val monthState = mutableStateOf(todayDate[1].toInt() - 1)
    val dayOfMonthState = mutableStateOf(todayDate[2].toInt())
    var nameSMS = ""
    var amountSMS = 0
    var yearSMS = 0
    var monthSMS = 0
    var daySMS = 0

    private val _deletedId: MutableStateFlow<List<Long>> = MutableStateFlow(emptyList())
    val deletedId = _deletedId.asStateFlow()

    fun setChipState(isSpend: Boolean) {
        _uiState.update {
            it.copy(
                isSpendState = isSpend
            )
        }
    }

    fun postCashData() {
        val currentName = name.value.text
        val currentAmount = amount.value.text
        val currentCategory = category.value
        _uiState.update {
            it.copy(
                needNameState = currentName.isEmpty(),
                needAmountState = currentAmount.isEmpty(),
                needDateState = yearState.value == 0 || monthState.value == 0 || dayOfMonthState.value == 0
            )
        }
        if (currentName.isEmpty() || currentAmount.isEmpty() || (yearState.value == 0 || monthState.value == 0 || dayOfMonthState.value == 0)) return
        viewModelScope.launch {
            val removedNotNumberAmount = currentAmount.replace(Regex("\\D+"), "")
            removedNotNumberAmount.toIntOrNull()?.let { userInputSpendAmount ->
                val spendTransact = when {
                    _uiState.value.isSpendState.not() -> {
                        History.Transact(
                            earnList = listOf(
                                History.Transact.TransactData(
                                    price = userInputSpendAmount,
                                    item = currentName,
                                    category = currentCategory
                                )
                            )
                        )
                    }
                    _uiState.value.isSpendState -> {
                        History.Transact(
                            spendList = listOf(
                                History.Transact.TransactData(
                                    price = userInputSpendAmount,
                                    item = currentName,
                                    category = currentCategory
                                )
                            )
                        )
                    }
                    else -> History.Transact()
                }
                val userInputHistory = History(
                    year = yearState.value,
                    month = monthState.value +1, // 달에는 1월을 추가해야 함.
                    date = dayOfMonthState.value,
                    dayOfWeek = "",
                    tool = "", // todo
                    memo = "", // todo
                    spendList = spendTransact
                )
                _uiState.update { it.copy(dismissDialogState = true) }
                repository.insertHistory(userInputHistory)
                category.value = ""
                name.value = TextFieldValue("")
                amount.value = TextFieldValue("")
            }
        }
    }

    fun postSMSData(){
        if (nameSMS == "" || amountSMS == 0 || (yearSMS == 0 || monthSMS == 0 || daySMS == 0)) return
        viewModelScope.launch {
            val spendTransact = History.Transact(
                spendList = listOf(
                    History.Transact.TransactData(
                        price = amountSMS,
                        item = nameSMS,
                        category = ""
                    )
                )
            )
            val userInputHistory = History(
                year = yearSMS,
                month = monthSMS,
                date = daySMS,
                dayOfWeek = "",
                tool = "", // todo
                memo = "", // todo
                spendList = spendTransact
            )
            //_uiState.update { it.copy(dismissDialogState = true) }
            repository.insertSms(userInputHistory)
            category.value = ""
        }
    }

    suspend fun deleteDataToEdit(
        historyId: Long?,
        transactionId: Long) {
        if (historyId == null) return
        /*viewModelScope.launch {
            kotlin.runCatching {
                repository.deleteTransactionData(historyId, transactionId)
            }.onSuccess {
                _deletedId.update {
                    it + transactionId
                }
            }.onFailure {
                Log.e("@@@PostDialogViewModel", "로그가 아닌 진짜 실패 : $it")
            }
        }*/
        repository.deleteTransactionData(historyId, transactionId)
    }

    fun refreshState() {
        _uiState.update { PostViewState() }
    }

    fun requestOcr(imageInputStream: InputStream?) {
        if (imageInputStream == null) return
        _uiState.update {
            it.copy(isLoading = true)
        }
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
            val receipt = response.receiptImageList.firstOrNull()?.receipt?.result
            name.value =
                TextFieldValue(receipt?.storeInfo?.name?.text.orEmpty())
            amount.value =
                TextFieldValue(receipt?.totalPrice?.price?.text.orEmpty())
            val date = receipt?.paymentInfo?.date?.formattedDate
            yearState.value = date?.year?.toIntOrNull() ?: 0
            monthState.value = (date?.month?.toIntOrNull() ?: 0) -1 // -1 해줘야 위에서 month 값 제대로 들어감
            dayOfMonthState.value = date?.day?.toIntOrNull() ?: 0
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    //카테고리 불러오기
    fun selectCategoryList(): List<String>? {
        return repository.getCategoryList()
    }
}
