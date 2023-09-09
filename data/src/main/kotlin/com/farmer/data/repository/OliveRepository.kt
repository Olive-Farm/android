package com.farmer.data.repository

import com.farmer.data.DateInfo
import com.farmer.data.History
import com.farmer.data.OliveDao
import com.farmer.data.network.model.ImageRequest
import com.farmer.data.network.model.ImageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface OliveRepository {
    fun getDateInfoByMonth(month: LocalDate): Flow<List<DateInfo>>
    suspend fun insertHistory(history: History)
    suspend fun getReceiptInformation(imageRequest: ImageRequest): ImageResponse

    suspend fun deleteHistory(history: History)


}