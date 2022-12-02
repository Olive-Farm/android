package com.farmer.home.model.response

import com.farmer.home.util.EMPTY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MonthSpendingList(
    @SerialName("day")
    val day: String = String.EMPTY,
    @SerialName("daySpendingList")
    val daySpendingList: List<DaySpendingList> = emptyList()
)
