package com.farmer.data

import androidx.room.*

@Dao
interface OliveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertSms(year: Int, month: Int, date: Int, price:Int, item:String ):History?

    @Query("SELECT * FROM HistoryList WHERE id = :id")
    suspend fun getHistoryById(id: Long): History?

    // todo year도 추가해야 함.
    @Query("SELECT * FROM HistoryList WHERE month = :month")
    suspend fun getHistoryByMonth(month: Int): List<History>?

    @Query("SELECT * FROM HistoryList WHERE year = :year AND month = :month AND date = :date")
    suspend fun getHistoryByDate(year: Int, month: Int, date: Int): History?

//    @Delete
//    suspend fun deleteHistory(history: History)

    @Query("DELETE FROM HistoryList WHERE id= :id")
    suspend fun deleteHistory(id: Long)


    //카테고리

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBasicList(categories: List<Category>)

    @Query("SELECT * FROM Category")
    fun getCategoryList(): List<Category>?

    @Query("DELETE FROM Category WHERE id = :id")
    suspend fun deleteCategory(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(category: Category)

}