package com.farmer.home.data

import com.farmer.home.data.remote.CashBookApi
import com.farmer.home.model.response.AllUserData
import com.farmer.network.BaseResponse
import javax.inject.Inject

class CashBookRepositoryImpl @Inject constructor(
    private val cashBookApi: CashBookApi
) : CashBookRepository {
    override suspend fun getAllUserData(): BaseResponse<List<AllUserData>> {
        return kotlin.runCatching {
            BaseResponse.Success(cashBookApi.getAllUserData())
        }.getOrElse {
            BaseResponse.Error(emptyList(), "Network error. Please try it again.")
        }
    }

    override suspend fun addCashData(time: String, name: String, amount: Int) {
        cashBookApi.addCashData(spTime = time, spName = name, spAmount = amount)
    }
}