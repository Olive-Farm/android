package com.farmer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "HistoryList")
data class History(
    @SerialName("year")
    val year: String = "", // 2023
    @SerialName("month")
    val month: String = "", // 3
    @SerialName("date")
    val date: String = "", // 10
    @SerialName("day_of_week")
    val dayOfWeek: String = "" , // MON, TUE
    @SerialName("tool")
    val tool: String = "" , // card, cash
    @SerialName("memo")
    val memo: String = "",
    @SerialName("category")
    val category: String = "", // 식비, 교통비
    @SerialName("price")
    val price: String = "",
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
