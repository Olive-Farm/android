package com.farmer.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context

@Database(entities = [History::class, Category::class], version = 1)
@TypeConverters(HistoryTypeConverter::class)
abstract class OliveDatabase : RoomDatabase() {
    abstract fun oliveDao(): OliveDao
    abstract fun categoryDao(): CategoryDao

}


