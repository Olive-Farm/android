package com.farmer.home.model.response

import com.farmer.home.util.EMPTY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DaySpendingList(
    @SerialName("time")
    val time: String = String.EMPTY,
    @SerialName("name")
    val name: String = String.EMPTY,
    @SerialName("amount")
    val amount: Int = 0,
    @SerialName("category")
    val category: String = String.EMPTY,
    @SerialName("method")
    val method: String = String.EMPTY,
    @SerialName("memo")
    val memo: String = String.EMPTY
)
