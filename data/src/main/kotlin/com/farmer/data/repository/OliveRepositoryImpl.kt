package com.farmer.data.repository

import com.farmer.data.DateInfo
import com.farmer.data.History
import com.farmer.data.OliveDao
import com.farmer.data.network.OliveApi
import com.farmer.data.network.model.ImageRequest
import com.farmer.data.network.model.ImageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class OliveRepositoryImpl @Inject constructor(
    private val dao: OliveDao,
    private val api: OliveApi
) : OliveRepository {

    /**
     * historyData와 달의 날짜를 가져와서 합쳐서 방출하는 함수
     */
    override fun getDateInfoByMonth(currentDate: LocalDate): Flow<List<DateInfo>> = flow {
        val historyData = getHistoryByCurrentDate(currentDate)
        val datesInMonth = getDatesInMonth(currentDate)
        val dateInfoList = buildList {
            datesInMonth.forEach { date ->
                val dateInfo = historyData.find { data ->
                    data.year == date.year &&
                            data.month == date.monthNumber &&
                            data.date == date.dayOfMonth
                }?.let {
                    DateInfo(date, it)
                } ?: DateInfo(date, null)
                add(dateInfo)
            }
        }
        emit(dateInfoList)
    }

    override suspend fun insertHistory(history: History) {
        val originHistory = dao.getHistoryByDate(
            year = history.year,
            month = history.month,
            date = history.date
        )
        if (originHistory != null) {
            val newSpendList = originHistory.spendList.spendList + history.spendList.spendList
            val newEarnedList = originHistory.spendList.earnList + history.spendList.earnList
            val newHistory = History(
                year = originHistory.year,
                month = originHistory.month,
                date = originHistory.date,
                dayOfWeek = originHistory.dayOfWeek,
                tool = originHistory.tool,
                memo = originHistory.memo,
                category = originHistory.category,
                spendList = History.Transact(
                    spendList = newSpendList,
                    earnList = newEarnedList
                ),
                id = originHistory.id
            )
            dao.insertHistory(history = newHistory)
        } else {
            dao.insertHistory(history = history)
        }
    }

    override suspend fun getReceiptInformation(imageRequest: ImageRequest): ImageResponse {
        return api.sendMessage(imageRequest)
    }

    private suspend fun getHistoryByCurrentDate(currentDate: LocalDate): List<History> {
        return dao.getHistoryByMonth(month = currentDate.month.value) ?: emptyList()
    }

    private fun getDatesInMonth(currentDate: LocalDate): List<LocalDate> {
        val yearMonth = YearMonth.parse("${currentDate.year}-${currentDate.monthNumber.addZero()}")
        val daysInMonth = yearMonth.month.length(yearMonth.isLeapYear)
        return (1..daysInMonth).map { LocalDate(yearMonth.year, yearMonth.month.value, it) }
    }

    // todo extension으로 빼기
    private fun Int.addZero(): String {
        return if (this in 0..9) {
            "0$this"
        } else {
            this.toString()
        }
    }
}