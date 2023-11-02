package com.farmer.home.data.remote

import com.farmer.data.network.model.ImageResponse
import com.farmer.home.model.response.AllUserData
import okhttp3.MultipartBody
import okhttp3.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface CashBookApi {

//    @Multipart
//    @POST("upload")
//    suspend fun uploadImage(
//        @Part image: MultipartBody.Part
//        // You can add more @Part annotations for additional fields if needed
//    ): Response<ImageResponse> // Define YourResponseModel as per your API response

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