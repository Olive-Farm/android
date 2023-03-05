package com.farmer.home.data

import com.farmer.home.model.response.AllUserData
import com.farmer.network.BaseResponse

interface CashBookRepository {

    suspend fun getAllUserData(): BaseResponse<List<AllUserData>>

    suspend fun addCashData(
        time: String,
        name: String,
        amount: Int
    )
}