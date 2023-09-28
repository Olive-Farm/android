package com.farmer.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [History::class, Category::class], version = 1) //어플 재설치를 안 한다면 version 2로 변경해야 함
@TypeConverters(HistoryTypeConverter::class)
abstract class OliveDatabase : RoomDatabase() {
    abstract fun oliveDao(): OliveDao
    //abstract fun categoryDao(): CategoryDao

}


