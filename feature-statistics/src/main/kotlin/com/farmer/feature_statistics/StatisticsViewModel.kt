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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
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
        //_isRefreshing.update { true }
        viewModelScope.launch {
            // 예: 사용자가 달력에서 월을 선택했을 때 호출되는 함수
            getTempChartDataList()
        }
        //_isRefreshing.update { false }
    }




    fun onNewDateSelect(year: Int, month: Int){
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

    // 카테고리별로 지출금액을 구하는 함수
    private suspend fun calculateTotalSpendByCategoryForMonth(): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        val staticList = repository.getStaticByMonth(uiState.value.month)


        for (history in staticList) {
            if (history.month == uiState.value.month) {
                for (transactData in history.spendList.spendList) {

                    // 카테고리 등록이 안 되어있을 경우,  Map '기타'로 매핑해주기
                    val category = if (transactData.category.isEmpty()) "기타" else transactData.category
                    val currentTotal = result.getOrDefault(transactData.category, 0)
                    result[category] = currentTotal + transactData.price
                }
            }
        }
        return result
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