package com.farmer.data.repository

import com.farmer.data.Category
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
import kotlin.random.Random

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

    override suspend fun insertSms(history: History) {
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

    override suspend fun insertHistory(history: History) {
        val originHistory = dao.getHistoryByDate(
            year = history.year,
            month = history.month,
            date = history.date
        )
        if (originHistory != null) {
            // 기존에 있던 내역
            val originSpendList = originHistory.spendList.spendList
            val originEarnList = originHistory.spendList.earnList

            // 새로 추가하려는 내역
            val newSpendList =
                originSpendList + history.spendList.spendList.mapIndexed { index, transactData ->
                    if (index == history.spendList.spendList.size - 1) {
                        transactData.copy(
                            id = Random.nextLong(1, 100000)
                        )
                    } else {
                        transactData
                    }
                }
            val newEarnedList =
                originEarnList + history.spendList.earnList.mapIndexed { index, transactData ->
                    if (index == history.spendList.earnList.size - 1) {
                        transactData.copy(
                            id = getRandomId()
                        )
                    } else {
                        transactData
                    }
                }

            // 새로운 내역
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
        } else { // 새로 넣는 값인 경우
            // 새로 넣는 값이 수입인 경우
            val idUpdatedHistory = if (history.spendList.earnList.isNotEmpty()) {
                history.copy(
                    spendList = history.spendList.copy(
                        earnList = history.spendList.earnList.mapIndexed { index, transactData ->
                            if (index == history.spendList.earnList.size - 1) {
                                transactData.copy(id = getRandomId())
                            } else {
                                transactData
                            }
                        }
                    )
                )
            } else if (history.spendList.spendList.isNotEmpty()) { // 새로 넣는 값이 지출인 경우
                history.copy(
                    spendList = history.spendList.copy(
                        spendList = history.spendList.spendList.mapIndexed { index, transactData ->
                            if (index == history.spendList.spendList.size - 1) {
                                transactData.copy(id = getRandomId())
                            } else {
                                transactData
                            }
                        }
                    )
                )
            } else {
                history
            }
            dao.insertHistory(history = idUpdatedHistory)
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

    override suspend fun deleteHistory(history: History) {
        dao.deleteHistory(history.id)
    }

    override suspend fun deleteTransactionData(
        historyId: Long,
        transactionId: Long
    ) {
        val originHistory = dao.getHistoryById(historyId) ?: return
        val newSpendList = originHistory.spendList.spendList.filterNot { it.id == transactionId }
        val newEarnList = originHistory.spendList.earnList.filterNot { it.id == transactionId }
        val newHistory = originHistory.copy(
            spendList = History.Transact(
                spendList = newSpendList,
                earnList = newEarnList
            )
        )
        dao.insertHistory(newHistory)
    }

    // todo extension으로 빼기
    private fun Int.addZero(): String {
        return if (this in 0..9) {
            "0$this"
        } else {
            this.toString()
        }
    }


    //카테고리
    override fun getCategoryList(): List<String> {
        val categoryList = dao.getCategoryList()
        val categoryTextList = mutableListOf<String>()
        return if (categoryList != null) {
            for (category in categoryList) {
                categoryTextList.add(category.categoryname)
            }
            categoryTextList
        } else emptyList()
    }

    override suspend fun insertCategory(category: Category) {

    }

    override suspend fun deleteCategory(category: Category) {

    }

    override suspend fun updateCategory(category: Category): Category {

        return TODO("반환 값을 제공하세요")
    }

    private fun getRandomId(): Long = Random.nextLong(1, 300000)
}