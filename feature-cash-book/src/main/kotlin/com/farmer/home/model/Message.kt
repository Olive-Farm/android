package com.farmer.home.model.response

import com.farmer.home.util.EMPTY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class Message(
    @SerialName("address")      //휴대폰 번호
    var address: String = String.EMPTY,
    @SerialName("timeStamp")    //시간
    var timeStamp: Long = 0,
    @SerialName("body")
    var body: String = String.EMPTY,
    @SerialName("year")    //시간
    var year: String = String.EMPTY
)