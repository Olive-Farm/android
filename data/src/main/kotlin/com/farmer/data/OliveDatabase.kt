package com.farmer.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [History::class, Category::class], version = 2)
@TypeConverters(HistoryTypeConverter::class)
abstract class OliveDatabase : RoomDatabase() {
    abstract fun oliveDao(): OliveDao
    //abstract fun categoryDao(): CategoryDao

}


