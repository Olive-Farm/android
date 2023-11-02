package com.farmer.feature_statistics

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.data.repository.OliveRepository
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.piechart.PieChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.min
import javax.inject.Inject


data class StatisticsUiState(
    val year: Int,
    val month: Int,
    val chartDataList: List<ChartData>,
    val barchartDataList : List<BarchartData>,
    val incomeList : List<IncomeData>,
    val spendList : List<SpendData>,
    val totalPrice : Int,
    val totalIncome : Int,
    val lastPrice : Int
) {
    data class ChartData(
        val categoryName: String,
        val percentage: Float,
        val color: Color,
        val count: Int,
        val spend: Int
    ) {
        fun toPieChartData(): PieChartData.Slice {
            return PieChartData.Slice(
                value = percentage,
                color = color
            )
        }
    }

    data class IncomeData(
        val incomePrice : Int
    )

    data class SpendData(
        val spendPrice : Int
    )

    data class BarchartData(
        val label : String,
        val value : Float,
        val color: Color
    ) {
        fun toBarChartData(): BarChartData.Bar {
            return BarChartData.Bar(
                label = label+"월",
                value = value,
                color = color
            )
        }
    }

    internal companion object {
        val EMPTY = StatisticsUiState(
            year = 0,
            month = 0,
            chartDataList = emptyList(),
            barchartDataList = emptyList(),
            totalPrice = 0,
            totalIncome = 0,
            lastPrice = 0,
            incomeList = emptyList(), // 통계용
            spendList = emptyList() // 통계용
        )
    }
}

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: OliveRepository,
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    val uiState = MutableStateFlow(StatisticsUiState.EMPTY)

    init {
        getCurrentYearMonth()
        viewModelScope.launch {
            getTempChartDataList()
            getCalculateMonthPrice()
            getStaticThreeMonth()
        }
    }

    fun refreshStatic() {
        viewModelScope.launch {
            // 예: 사용자가 달력에서 월을 선택했을 때 호출되는 함수
            getTempChartDataList()
            getCalculateMonthPrice()
            getStaticThreeMonth()
        }
    }


    fun onNewDateSelect(year: Int, month: Int) {
        uiState.update {
            it.copy(
                year = year,
                month = month + 1 // 달을 0부터 카운트하는 것 같아서 1 추가
            )
        }
    }

    private fun getCurrentYearMonth() {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        uiState.update {
            it.copy(
                year = now.year,
                month = now.monthNumber
            )
        }
    }

    // 지난달과 지출금액을 비교하는 함수
    suspend fun compareMonthPrice(): Int {
        var lastPrice = 0
        val lastList = repository.getStaticByMonth(uiState.value.month - 1)

        for (history in lastList) {
            if (history.month == uiState.value.month - 1) {
                for (transactData in history.spendList.spendList) {
                    lastPrice += transactData.price
                }
            }
        }
        return lastPrice

    }


    // 카테고리별로 가장 많이 카운트 된 카테고리를 return 하는 함수
    fun findCategoryWithMaxCount(chartDataList: List<StatisticsUiState.ChartData>): String {
        val categoryCountMap = chartDataList.groupBy { it.categoryName }
            .mapValues { it.value.sumBy { data -> data.count } }

        return categoryCountMap.maxByOrNull { it.value }?.key ?: "No category found"
    }

    // 카테고리별로 지출금액과 카운트를 구하는 함수
    private suspend fun calculateTotalSpendByCategoryForMonth(): Pair<Map<String, Int>, Map<String, Int>> {
        val result = mutableMapOf<String, Int>() // 카테고리별 총 지출 금액
        val categoryCount = mutableMapOf<String, Int>() // 카테고리별 등장 횟수
        val staticList = repository.getStaticByMonth(uiState.value.month)

        for (history in staticList) {
            if (history.month == uiState.value.month) {
                for (transactData in history.spendList.spendList) {
                    val category =
                        if (transactData.category.isEmpty()) "미분류" else transactData.category
                    val currentTotal = result.getOrDefault(category, 0)
                    result[category] = currentTotal + transactData.price

                    // 카테고리별 등장 횟수 카운트
                    val currentCount = categoryCount.getOrDefault(category, 0)
                    categoryCount[category] = currentCount + 1
                }
            }
        }
        return Pair(result, categoryCount)
    }

    // 월별 총 지출금액을 구하는 함수
    private suspend fun calculateTotalMonthlySpend(): Int {
        var totalSpend = 0

        val staticlist = repository.getStaticByMonth(uiState.value.month)

        for (history in staticlist) {
            if (history.month == uiState.value.month) {
                for (transactData in history.spendList.spendList) {
                    totalSpend += transactData.price
                }
            }
        }

        return totalSpend
    }

    // 월별 총 수입금액을 구하는 삼수
    private suspend fun calculateTotalMonthlyIncome(): Int {
        var totalIncome = 0

        val staticlist = repository.getStaticByMonth(uiState.value.month)

        for (history in staticlist) {
            if (history.month == uiState.value.month) {
                for (transactData in history.spendList.earnList) {
                    totalIncome += transactData.price
                }
            }
        }
        return totalIncome
    }

    // 월별 수입, 지출 금액을 update 하는 함수
    private suspend fun getCalculateMonthPrice() {
        val totalSpend = calculateTotalMonthlySpend()
        val totalIncome = calculateTotalMonthlyIncome()
        val lastPrice = compareMonthPrice()
        uiState.update {
            it.copy(totalPrice = totalSpend, totalIncome = totalIncome, lastPrice = lastPrice)
        }
    }


    // 해당 년도의 월별 통계를 보여주는 함수
    private suspend fun calculateMonth(): Map<Int, Int> {
        val totalSpendMap = mutableMapOf<Int, Int>()
        val monthList = repository.getMonthStatic(uiState.value.year)

        for (history in monthList) {
            if (history.year == uiState.value.year) {
                for (transactData in history.spendList.spendList) {
                    val month = history.month
                    val monthTotal = totalSpendMap.getOrDefault(history.month, 0)
                    totalSpendMap[month] = monthTotal + transactData.price
                }
            }
        }

        return totalSpendMap
    }


    private fun getColorForCategory(category: String): Color {
        return when (category) {
            "식비" -> Color1
            "교통비" -> Color2
            "쇼핑" -> Color3
            "급여" -> Color4
            "용돈" -> Color5
            "이체" -> Color6
            "카드대금" -> Color7
            "고정지출" -> Color8
            "저축" -> Color9
            "여가비" -> Color10
            "생활비" -> Color1
            "병원/의료비" -> Color2
            "문화/관광비" -> Color3
            "기타" -> Color10
            else -> ColorGray
        }
    }


    private suspend fun getTempChartDataList() {

        val (totalSpendByCategory, categoryCount) = calculateTotalSpendByCategoryForMonth()
        val totalMonthlySpend = calculateTotalMonthlySpend()

        val chartDataList = totalSpendByCategory.map { (category, totalSpend) ->
            val percentage = (totalSpend.toFloat() / totalMonthlySpend) * 100
            val color = getColorForCategory(category)
            StatisticsUiState.ChartData(
                categoryName = category,
                percentage = percentage,
                color = color,
                count = categoryCount[category] ?: 0,
                spend = totalSpend
            )
        }

        uiState.update {
            it.copy(chartDataList = chartDataList)
        }

    }

//    private suspend fun getMonthTotalDataList() {
//
//        val totalMonthSpend = calculateMonth()
//
//        val monthDataList = totalMonthSpend.map { (month, totalSpend) ->
//            val color = getColorForMonth(month)
//            StatisticsUiState.BarchartData(
//                label = month.toString(),
//                value = totalSpend.toFloat(),
//                color = color
//            )
//        }
//        uiState.update {
//            it.copy(barchartDataList = monthDataList)
//        }
//    }

    private suspend fun getStaticThreeMonth() {

        val incomeList = mutableListOf<StatisticsUiState.IncomeData>()
        val spendList = mutableListOf<StatisticsUiState.SpendData>()

        for (i in 0 until 3) {
            val monthList = repository.getStaticByMonth(uiState.value.month - i)

            var totalSpend = 0
            var totalIncome = 0

            for (history in monthList) {
                if (history.month == uiState.value.month - i) {

                    for (transactData in history.spendList.spendList) {
                        totalSpend += transactData.price

                    }

                    for (transactData in history.spendList.earnList) {
                        totalIncome += transactData.price

                    }
                }
            }
            spendList.add(StatisticsUiState.SpendData(totalSpend))
            incomeList.add(StatisticsUiState.IncomeData(totalIncome))
        }
        uiState.update {
            it.copy(spendList = spendList, incomeList = incomeList)
        }

    }
}
