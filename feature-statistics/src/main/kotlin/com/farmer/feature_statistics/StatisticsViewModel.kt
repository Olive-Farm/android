package com.farmer.feature_statistics

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.farmer.data.repository.OliveRepository
import com.github.tehras.charts.piechart.PieChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class StatisticsUiState(
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
        val EMPTY = StatisticsUiState(emptyList())
    }
}

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: OliveRepository
) : ViewModel() {

    val uiState = MutableStateFlow(StatisticsUiState.EMPTY)

    init {
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