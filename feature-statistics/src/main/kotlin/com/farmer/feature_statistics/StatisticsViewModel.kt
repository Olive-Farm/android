package com.farmer.feature_statistics

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.farmer.data.repository.OliveRepository
import com.github.tehras.charts.piechart.PieChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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
                    StatisticsUiState.ChartData(
                        categoryName = "공부",
                        percentage = 25f,
                        color = Green300
                    ),
                    StatisticsUiState.ChartData(
                        categoryName = "쇼핑",
                        percentage = 40f,
                        color = Green500
                    ),
                    StatisticsUiState.ChartData(
                        categoryName = "음식",
                        percentage = 30f,
                        color = Green700
                    ),
                )
            )
        }
    }
}