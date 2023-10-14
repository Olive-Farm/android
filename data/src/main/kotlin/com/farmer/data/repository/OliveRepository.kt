package com.farmer.data.repository

import com.farmer.data.Category
import com.farmer.data.DateInfo
import com.farmer.data.History
import com.farmer.data.network.model.ImageRequest
import com.farmer.data.network.model.ImageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface OliveRepository {
    fun getDateInfoByMonth(month: LocalDate): Flow<List<DateInfo>>
    suspend fun insertHistory(history: History)
    suspend fun getReceiptInformation(imageRequest: ImageRequest): ImageResponse

    suspend fun deleteHistory(history: History)

    suspend fun deleteTransactionData(historyId: Long, transactionId: Long)

    suspend fun insertSms(history: History)

    suspend fun getStaticByMonth(month: Int): List<History>


    //카테고리
    fun getCategoryList(): List<String>?

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(category: Category): Category


}