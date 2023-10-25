package com.example.feature_post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName("address")      //휴대폰 번호
    var address: String = "",
    @SerialName("timeStamp")    //시간
    var timeStamp: Long = 0,
    @SerialName("body")
    var body: String = "",
    @SerialName("year")    //시간
    var year: String = ""
)