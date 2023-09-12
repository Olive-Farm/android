package com.farmer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OliveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertSms(year: Int, month: Int, date: Int, price:Int, item:String ):History?

    // todo year도 추가해야 함.
    @Query("SELECT * FROM HistoryList WHERE month = :month")
    suspend fun getHistoryByMonth(month: Int): List<History>?

    @Query("SELECT * FROM HistoryList WHERE year = :year AND month = :month AND date = :date")
    suspend fun getHistoryByDate(year: Int, month: Int, date: Int): History?

//    @Delete
//    suspend fun deleteHistory(history: History)

    @Query("DELETE FROM HistoryList WHERE id= :id")
    suspend fun deleteHistory(id: Long)


}