package com.farmer.home.data

import com.farmer.home.model.response.AllUserData

interface CashBookRepository {

    suspend fun getAllUserData(): List<AllUserData>

    suspend fun addCashData(
        time: String,
        name: String,
        amount: Int
    )
}