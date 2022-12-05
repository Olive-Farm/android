package com.farmer.home.data.remote

import com.farmer.home.model.response.AllUserData
import retrofit2.http.GET

interface CashBookApi {

    @GET("list/user")
    suspend fun getAllUserData(): List<AllUserData>
}