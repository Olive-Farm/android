package com.farmer.home.data

import com.farmer.home.data.remote.CashBookApi
import com.farmer.home.model.response.AllUserData
import javax.inject.Inject

class CashBookRepositoryImpl @Inject constructor(
    private val cashBookApi: CashBookApi
) : CashBookRepository {
    override suspend fun getAllUserData(): List<AllUserData> {
        return cashBookApi.getAllUserData()
    }
}