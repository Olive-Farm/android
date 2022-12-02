package com.farmer.home.data

import com.farmer.home.model.response.AllUserDataResponse

internal interface CashBookRepository {

    suspend fun getAllUserData(): AllUserDataResponse
}