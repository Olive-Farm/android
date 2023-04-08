package com.farmer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class HistoryList(
    val year: String = "",
    val month: String = "",
    val date : String = "",
    val dayOfWeek: String = "",
    val tool: String = "",
    val memo: String = "",
    val category: String = "",
    val id: Long = 0
)