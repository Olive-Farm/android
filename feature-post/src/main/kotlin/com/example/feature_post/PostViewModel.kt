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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
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

    val name = mutableStateOf(TextFieldValue(""))
    val amount = mutableStateOf(TextFieldValue(""))
    val category = mutableStateOf(String())
    val yearState = mutableStateOf(0)
    val monthState = mutableStateOf(-1)
    val dayOfMonthState = mutableStateOf(0)
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

    // 멀티데이터 버전
//    fun requestOcr(imageInputStream: InputStream?, clientSecret: String) {
//        if (imageInputStream == null) return
//        _uiState.update {
//            it.copy(isLoading = true)
//        }
//
//        val httpClient = OkHttpClient.Builder().build()
//
//        val requestBody = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart(
//                "image",
//                "image.png",
//                imageInputStream.asRequestBody("image/*".toMediaTypeOrNull())
//            )
//            .build()
//
//        val request = Request.Builder()
//            .url("https://aeox4la6gs.apigw.ntruss.com/custom/v1/")
//            .addHeader("X-OCR-SECRET", clientSecret)
//            .post(requestBody)
//            .build()
//
//        viewModelScope.launch {
//            val selectedImage = BitmapFactory.decodeStream(imageInputStream)
//            val stream = ByteArrayOutputStream()
//            selectedImage.compress(Bitmap.CompressFormat.PNG, 10, stream)
//            val image = stream.toByteArray()
//
//            val imageString = Base64.encodeToString(image, 0)
//            Log.e("@@@encdedImage", "size : ${imageString.length}")
//            val response = repository.getReceiptInformation(
//                ImageRequest(
//                     version = "V2",
//                    requestId = UUID.randomUUID().toString(),
//                    timestamp = (Instant.now().toEpochMilli()),
//                    images = listOf(
//                        Image(
//                        format = "png",
//                        name = "test 1",
//                        data = imageString
//                        )
//                    )
//                )
//            )
//            val receipt = response.receiptImageList.firstOrNull()?.receipt?.result
//            name.value =
//                TextFieldValue(receipt?.storeInfo?.name?.text.orEmpty())
//            amount.value =
//                TextFieldValue(receipt?.totalPrice?.price?.text.orEmpty())
//            val date = receipt?.paymentInfo?.date?.formattedDate
//            yearState.value = date?.year?.toIntOrNull() ?: 0
//            monthState.value = (date?.month?.toIntOrNull() ?: 0) -1 // -1 해줘야 위에서 month 값 제대로 들어감
//            dayOfMonthState.value = date?.day?.toIntOrNull() ?: 0
//            _uiState.update {
//                it.copy(isLoading = false)
//            }
//        }
//    }

    fun requestOcr(imageInputStream: InputStream?) {
        if (imageInputStream == null) return

        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val selectedImage = BitmapFactory.decodeStream(imageInputStream)
            val compressedImage = compressImage(selectedImage) // 이미지 압축
            val imageString = encodeImageToString(compressedImage) // 이미지를 문자열로 변환

            Log.e("@@@encdedImage", "size : ${imageString.length}")

            val response = repository.getReceiptInformation(
                ImageRequest(
                    version = "V2",
                    requestId = UUID.randomUUID().toString(),
                    timestamp = Instant.now().toEpochMilli(),
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

            // 데이터 처리
            name.value = TextFieldValue(receipt?.storeInfo?.name?.text.orEmpty())
            amount.value = TextFieldValue(receipt?.totalPrice?.price?.text.orEmpty())
            val date = receipt?.paymentInfo?.date?.formattedDate
            yearState.value = date?.year?.toIntOrNull() ?: 0
            monthState.value = (date?.month?.toIntOrNull() ?: 0) - 1 // -1 해줘야 위에서 month 값 제대로 들어감
            dayOfMonthState.value = date?.day?.toIntOrNull() ?: 0

            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    // 이미지 압축 함수
    private suspend fun compressImage(image: Bitmap): Bitmap {
        return withContext(Dispatchers.Default) {
            val stream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 10, stream) // 압축 수준 변경 (0~100 사이의 값으로 변경)
            val byteArray = stream.toByteArray()
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

    // 이미지를 Base64 문자열로 인코딩하는 함수
    private fun encodeImageToString(image: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream) // 압축 수준 변경 (0~100 사이의 값으로 변경)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    //카테고리 불러오기
    fun selectCategoryList(): List<String>? {
        return repository.getCategoryList()
    }
}
