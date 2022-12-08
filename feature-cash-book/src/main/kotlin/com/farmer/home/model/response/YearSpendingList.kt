package com.farmer.home.model.response

import com.farmer.home.util.EMPTY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YearSpendingList(
    @SerialName("month")
    val month: String = String.EMPTY,
    @SerialName("monthSpendingList")
    val monthSpendingList: List<MonthSpendingList> = emptyList()
)
