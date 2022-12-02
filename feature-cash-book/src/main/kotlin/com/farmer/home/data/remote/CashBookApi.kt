package com.farmer.home.data.remote

import com.farmer.home.model.response.AllUserDataResponse
import retrofit2.http.GET

internal interface CashBookApi {

    @GET("/list/user")
    suspend fun getAllUserData(): AllUserDataResponse
}