package com.farmer.home.model.response

import com.farmer.home.util.EMPTY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUserDataResponse(
    @SerialName("userId")
    val userId: String = String.EMPTY,
    @SerialName("userSpendingList")
    val userSpendingList: List<UserSpendingList> = emptyList()
)