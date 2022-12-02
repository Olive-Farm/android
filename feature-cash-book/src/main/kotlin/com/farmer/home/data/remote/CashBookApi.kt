package com.farmer.home.data.remote

import retrofit2.http.GET

interface CashBookApi {
    // todo
    @GET("/list/user")
    suspend fun temp()
}