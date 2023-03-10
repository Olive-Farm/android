package com.farmer.data.repository

import com.farmer.data.History

interface OliveRepository {
    suspend fun getHistoryByMonth(month: String):List<History>
    suspend fun insertHistory(history: History)
}