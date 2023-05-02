package com.farmer.data.network.model

import kotlinx.serialization.SerialName

//{
//  "version": "V2",
//  "requestId": "string",
//  "timestamp": 0,
//  "images": [
//    {
//      "format": "jpg",
//      "name": "test 1",
//      "data": "data"
//    }
//  ]
//}
@kotlinx.serialization.Serializable
data class ImageRequest(
    @SerialName("images")
    val images: Image,
    @SerialName("timestamp")
    val timestamp: Long = 0L,
    @SerialName("requestId")
    val requestId: String = "String",
    @SerialName("version")
    val version: String
)
