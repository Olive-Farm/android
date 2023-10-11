package com.farmer.feature_statistics

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        getTempChartDataList()
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

    // 실제 데이터를 가져오면 이 함수는 지워야 함. 임시로 데이터 가져오는 함수.
    private fun getTempChartDataList() {
        uiState.update {
            it.copy(
                chartDataList = listOf(
                    StatisticsUiState.ChartData(
                        categoryName = "쇼핑",
                        percentage = 15f,
                        color = Green100
                    ),
                )
            )
        }
    }
}