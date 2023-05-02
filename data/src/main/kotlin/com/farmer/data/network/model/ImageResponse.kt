package com.farmer.data.network.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ImageResponse(
    @SerialName("version")
    val version: String,
    @SerialName("requestId")
    val requestId: String,
    @SerialName("timestamp")
    val timestamp: String,
    @SerialName("images")
    val receiptImageList: List<Receipt>
)
