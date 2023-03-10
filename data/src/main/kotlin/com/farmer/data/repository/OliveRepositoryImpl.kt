package com.farmer.data.repository

import com.farmer.data.History
import com.farmer.data.OliveDao
import javax.inject.Inject

class OliveRepositoryImpl @Inject constructor(private val dao: OliveDao) : OliveRepository {
    override suspend fun getHistoryByMonth(month: String): List<History> {
        return dao.getHistoryByMonth(month = month)
    }

    override suspend fun insertHistory(history: History) {
        dao.insertHistory(history = history)
    }
}