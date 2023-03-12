package com.farmer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "HistoryList")
data class History(
    @SerialName("year")
    val year: Int = -1, // 2023
    @SerialName("month")
    val month: Int = -1, // 3
    @SerialName("date")
    val date: Int = -1, // 10
    @SerialName("day_of_week")
    val dayOfWeek: String = "", // MON, TUE
    @SerialName("tool")
    val tool: String = "", // card, cash
    @SerialName("memo")
    val memo: String = "",
    @SerialName("category")
    val category: String = "", // 식비, 교통비
    @SerialName("transact")
    val spendList: Transact,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
) {

    @Serializable
    data class Transact(
        @SerialName("spend_list")
        val spendList: List<TransactData>,
        @SerialName("earn_list")
        val earnList: List<TransactData>
    ) {
        @Serializable
        data class TransactData(
            @SerialName("price")
            val price: Int,
            @SerialName("item")
            val item: String
        )
    }
}
