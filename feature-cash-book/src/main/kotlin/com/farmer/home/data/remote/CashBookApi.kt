package com.farmer.home.data.remote

import com.farmer.home.model.response.AllUserData
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PUT

interface CashBookApi {

    @GET("list/user")
    suspend fun getAllUserData(): List<AllUserData>

    // since body type is not working, we have to send data with @Field annotation.
    @FormUrlEncoded
    @PUT("list/insert")
    suspend fun addCashData(
        @Field("userID") userID: String = "3",
        @Field("spTime") spTime: String,
        @Field("spName") spName: String,
        @Field("spAmount") spAmount: Int,
        @Field("spCategory") spCategory: String = "",
        @Field("spMethod") spMethod: String = "",
        @Field("spMemo") spMemo: String = "",
    )
}