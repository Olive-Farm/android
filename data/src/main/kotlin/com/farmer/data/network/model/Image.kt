package com.farmer.data.network.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Image(
    @SerialName("format")
    val format: String,//  ImageType,
    @SerialName("name")
    val name: String,
    @SerialName("data")
    val data: String
)
