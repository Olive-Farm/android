package com.farmer.data.network

import com.farmer.data.network.model.ImageRequest
import com.farmer.data.network.model.ImageResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface OliveApi {
//   @Headers()
    //@POST("21725/2219f9ef9c633b3041d286522a468b3e5334eeb32bece875e8ff7ece347ca107/document/receipt/")
    @POST("25426/975d2047eea53e89ff1183d90a1eab85944bcb5d050863c99879b6859b1e7824/document/receipt/")
    suspend fun sendMessage(
        @Body imageRequest: ImageRequest,
//        @Query("?") tempString: String,
        // todo 아래 토큰은 공유되면 안됨!
        @Header("X-OCR-SECRET") token: String = "S3N6dGZHTlhmcHFMUVVnTWtQd1lLWFhMbHBpWGhLZGI=",
        @Header("Content-Type") contentType: String = "application/json"
    ): ImageResponse
}
