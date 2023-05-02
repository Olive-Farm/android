package com.farmer.data.network.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class ImageType {
    @SerialName("jpg")
    JPG,
    @SerialName("jpeg")
    JPEG,
    @SerialName("png")
    PNG;
}