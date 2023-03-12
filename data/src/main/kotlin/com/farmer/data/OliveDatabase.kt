package com.farmer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [History::class], version = 1)
@TypeConverters(HistoryTypeConverter::class)
abstract class OliveDatabase : RoomDatabase() {
    abstract fun oliveDao(): OliveDao
}
