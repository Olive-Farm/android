package com.farmer.data

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HistoryTypeConverter {

    private val json = Json

    @TypeConverter
    fun fromHistory(value: History?): String? {
        return if (value == null) null else json.encodeToString(value)
    }

    @TypeConverter
    fun toHistory(value: String?): History? {
        return if (value == null) null else json.decodeFromString(value)
    }

    @TypeConverter
    fun fromTransact(value: History.Transact?): String? {
        return if (value == null) null else json.encodeToString(value)
    }

    @TypeConverter
    fun toTransact(value: String?): History.Transact? {
        return if (value.isNullOrBlank()) null else json.decodeFromString(value)
    }
}
