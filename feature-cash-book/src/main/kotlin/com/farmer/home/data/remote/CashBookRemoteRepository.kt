package com.farmer.home.data.remote

import com.farmer.home.model.response.AllUserData
import javax.inject.Inject

internal class CashBookRemoteRepository @Inject constructor(
    private val cashBookApi: CashBookApi
) {
    suspend fun getAllUserData(): List<AllUserData> {
        return cashBookApi.getAllUserData()
    }
}