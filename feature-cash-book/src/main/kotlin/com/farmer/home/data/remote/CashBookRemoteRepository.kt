package com.farmer.home.data.remote

import com.farmer.home.model.response.AllUserDataResponse
import javax.inject.Inject

internal class CashBookRemoteRepository @Inject constructor(
    private val cashBookApi: CashBookApi
) {
    suspend fun getAllUserData(): AllUserDataResponse {
        return cashBookApi.getAllUserData()
    }
}