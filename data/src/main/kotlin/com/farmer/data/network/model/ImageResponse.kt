package com.farmer.data.network.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ImageResponse(
    @SerialName("version")
    val version: String,
    @SerialName("requestId")
    val requestId: String,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("images")
    val receiptImageList: List<ImagesV2>
)

@kotlinx.serialization.Serializable
data class ImagesV2(
    @SerialName("name")
    val name: String? = null,
    @SerialName("receipt")
    val receipt: Images? = Images()
)
