package com.farmer.data.repository

import com.farmer.data.DateInfo
import com.farmer.data.History
import com.farmer.data.OliveDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class OliveRepositoryImpl @Inject constructor(
    private val dao: OliveDao
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
        dao.insertHistory(history = history)
    }

    private suspend fun getHistoryByCurrentDate(currentDate: LocalDate): List<History> {
        return dao.getHistoryByMonth(month = currentDate.month.value)
    }

    private fun getDatesInMonth(currentDate: LocalDate): List<LocalDate> {
        val yearMonth = YearMonth.parse("${currentDate.year}-${currentDate.monthNumber}")
        val daysInMonth = yearMonth.month.length(yearMonth.isLeapYear)
        return (1..daysInMonth).map { LocalDate(yearMonth.year, yearMonth.month.value, it) }
    }
}