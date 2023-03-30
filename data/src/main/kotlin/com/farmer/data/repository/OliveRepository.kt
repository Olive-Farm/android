package com.farmer.data.repository

import com.farmer.data.DateInfo
import com.farmer.data.History
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface OliveRepository {
    fun getDateInfoByMonth(month: LocalDate): Flow<List<DateInfo>>
    suspend fun insertHistory(history: History)
}