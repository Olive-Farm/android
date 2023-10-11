package com.farmer.feature_statistics

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmer.feature_statistics.StatisticsUiState.ChartData
import java.util.Calendar

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
)
{
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SelectDate(
            year = uiState.year,
            month = uiState.month,
            onNewDateSelect = viewModel::onNewDateSelect
        )

        PieChart(
            modifier = Modifier
                .height(280.dp)
                .padding(top = 24.dp),
            pieChartData = PieChartData(
                uiState.chartDataList.map { it.toPieChartData() }
            ),
            animation = simpleChartAnimation(),
            sliceDrawer = SimpleSliceDrawer(14f) // thickness 조정 가능
        )

        LazyColumn(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.chartDataList) { chartData ->
                ChartDataCategoryItem(chartData)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SelectDate(
    year: Int,
    month: Int,
    onNewDateSelect: (year: Int, month: Int) -> Unit
) {
    val calendar = Calendar.getInstance()
    val timePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, selectedYear, selectedMonth, _ ->
            onNewDateSelect(selectedYear, selectedMonth)
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH],
    )

    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .clickable {
                timePickerDialog.show()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${year}년 ${month}월 ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
    }
}

@Composable
fun ChartDataCategoryItem(chartData: ChartData) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(chartData.color)
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = chartData.categoryName,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = " (${chartData.percentage.toInt()}%)",
            color = Color.Black
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
    }
}