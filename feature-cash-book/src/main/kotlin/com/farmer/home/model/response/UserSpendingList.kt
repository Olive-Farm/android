package com.farmer.home.model.response

import com.farmer.home.util.EMPTY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserSpendingList(
    @SerialName("year")
    val year: String = String.EMPTY,
    @SerialName("yearSpendingList")
    val yearSpendingList: List<YearSpendingList> = emptyList()
)