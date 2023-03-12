package com.farmer.data

import androidx.room.TypeConverter

class HistoryTypeConverter {
    @TypeConverter
    fun fromTransact(transact: History.Transact): String {
        return "${transact.spendList},${transact.earnList}"
    }

    @TypeConverter
    fun toTransact(value: String): History.Transact {
        val (spendListString, earnListString) = value.split(",")
        val spendList = spendListString.split(";").map {
            val (price, item) = it.split("|")
            History.Transact.TransactData(price.toInt(), item)
        }
        val earnList = earnListString.split(";").map {
            val (price, item) = it.split("|")
            History.Transact.TransactData(price.toInt(), item)
        }
        return History.Transact(spendList, earnList)
    }
}