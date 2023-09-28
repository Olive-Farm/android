package com.farmer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Category")
data class Category(
    @SerialName("categoryname")
    val categoryname: String = "",
    @PrimaryKey(autoGenerate = true)
    @SerialName("id")
    val id: Int = 0
)
