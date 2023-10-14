package com.farmer.feature_statistics

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.data.Category
import com.farmer.data.History
import com.farmer.data.repository.OliveRepository
import com.github.tehras.charts.piechart.PieChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

data class StatisticsUiState(
    val year: Int,
    val month: Int,
    val chartDataList: List<ChartData>
) {
    data class ChartData(
        val categoryName: String,
        val percentage: Float,
        val color: Color
    ) {
        fun toPieChartData(): PieChartData.Slice {
            return PieChartData.Slice(
                value = percentage,
                color = color
            )
        }
    }

    internal companion object {
        val EMPTY = StatisticsUiState(
            year = 0,
            month = 0,
            chartDataList = emptyList()
        )
    }
}

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: OliveRepository
) : ViewModel() {

    val uiState = MutableStateFlow(StatisticsUiState.EMPTY)

    init {
        getCurrentYearMonth()
        viewModelScope.launch {
            getTempChartDataList()
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

    private suspend fun calculateTotalSpendByCategoryForMonth(): Map<String, Int> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val result = mutableMapOf<String, Int>()

        val staticList = repository.getStaticByMonth(now.monthNumber)

        for (history in staticList) {
            if (history.month == now.monthNumber) {
                for (transactData in history.spendList.spendList) {
                    val currentTotal = result.getOrDefault(transactData.category, 0)
                    result[transactData.category] = currentTotal + transactData.price
                }
            }
        }

        return result
    }

    private suspend fun calculateTotalMonthlySpend(): Int {
        var totalSpend = 0
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            val staticlist = repository.getStaticByMonth(now.monthNumber)

            for (history in staticlist) {
                if (history.month == now.monthNumber) {
                    for (transactData in history.spendList.spendList) {
                        totalSpend += transactData.price
                    }
                }
            }
        return totalSpend
    }

    private fun getColorForCategory(category: String): Color {
        return when (category) {
            "식비" -> Green50
            "교통비" -> Green100
            "쇼핑" -> Green200
            "급여"-> Green300
            "용돈"-> Green400
            "이체"-> Green500
            "카드대금"-> Green700
            "고정지출"-> Green800
            "저축"-> Green200
            "여가비"-> Green200
            "생활비"-> Green200
            "병원/의료비"-> Green200
            "문화/관광비"-> Green200
            "기타"-> Green200
            else -> GreenTheme
        }
    }


    private suspend fun getTempChartDataList() {

        val totalSpendByCategory = calculateTotalSpendByCategoryForMonth()
        val totalMonthlySpend = calculateTotalMonthlySpend()

        val chartDataList = totalSpendByCategory.map { (category, totalSpend) ->
            val percentage = (totalSpend.toFloat() / totalMonthlySpend) * 100
            val color = getColorForCategory(category)
            StatisticsUiState.ChartData(categoryName = category, percentage = percentage, color = color)
        }
        uiState.update {
            it.copy(chartDataList = chartDataList)
        }




    }
}