package com.farmer.home.data

import com.farmer.home.data.remote.CashBookApi
import com.farmer.home.model.response.AllUserDataResponse
import javax.inject.Inject

internal class CashBookRepositoryImpl @Inject constructor(
    val cashBookApi: CashBookApi
): CashBookRepository {
    override suspend fun getAllUserData(): AllUserDataResponse {
        return cashBookApi.getAllUserData()
    }
}