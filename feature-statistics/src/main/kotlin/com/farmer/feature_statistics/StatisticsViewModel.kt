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
import javax.inject.Inject
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryOf

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
        val count: Int
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
        }
    }

    fun refreshStatic() {
        viewModelScope.launch {
            // 예: 사용자가 달력에서 월을 선택했을 때 호출되는 함수
            getTempChartDataList()
            getMonthTotalDataList()
            getCalculateMonthPrice()
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
    suspend fun compareMonthPrice():Int {
        var lastPrice = 0
        val lastList =  repository.getStaticByMonth(uiState.value.month-1)

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
        val categoryCountMap = chartDataList.groupBy { it.categoryName }.mapValues { it.value.sumBy { data -> data.count } }

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
                    val category = if (transactData.category.isEmpty()) "미분류" else transactData.category
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
        var totalIncome= 0

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
        val totalSpend =  calculateTotalMonthlySpend()
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
            "식비" -> Green50
            "교통비" -> Green100
            "쇼핑" -> Green200
            "급여" -> Green300
            "용돈" -> Green400
            "이체" -> Green500
            "카드대금" -> Green700
            "고정지출" -> Green800
            "저축" -> Green200
            "여가비" -> Green200
            "생활비" -> Green200
            "병원/의료비" -> Green200
            "문화/관광비" -> Green200
            "기타" -> Green200
            else -> GreenTheme
        }
    }

    private fun getColorForMonth(month: Int): Color {
        return when (month) {
            1 -> Green50
            2 -> Green100
            3 -> Green200
            4 -> Green300
            5 -> Green400
            6 -> Green500
            7 -> Green700
            8 -> Green700
            9 -> Green800
            10 -> Green50
            11 -> Green100
            12 -> Green200
            else -> GreenTheme
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
                count = categoryCount[category] ?: 0 // 카테고리별 등장 횟수 추가
            )
        }

        uiState.update {
            it.copy(chartDataList = chartDataList)
        }

    }

    private suspend fun getMonthTotalDataList() {

        val totalMonthSpend = calculateMonth()

        val monthDataList = totalMonthSpend.map { (month, totalSpend) ->
            val color = getColorForMonth(month)
            StatisticsUiState.BarchartData(
                label = month.toString(),
                value = totalSpend.toFloat(),
                color = color
            )
        }
        uiState.update {
            it.copy(barchartDataList = monthDataList)
        }
    }

    suspend fun getStaticThreeMonth() {

    }



}
