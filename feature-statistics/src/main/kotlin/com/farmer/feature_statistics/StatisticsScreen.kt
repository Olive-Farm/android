package com.farmer.feature_statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import androidx.compose.foundation.layout.padding

@Composable
fun StatisticsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        PieChart(
            modifier = Modifier
                .height(120.dp)
                .padding(top = 16.dp),
            pieChartData = PieChartData(
                // todo 아래에 추가하면 됨.
                listOf(
                    PieChartData.Slice(
                        15f,
                        Green400
                    ),
                    PieChartData.Slice(
                        35f,
                        Green200
                    ),
                    PieChartData.Slice(
                        50f,
                        Green700
                    ),
                )
            ),
            animation = simpleChartAnimation(),
            sliceDrawer = SimpleSliceDrawer(25f) // thickness 조정 가능
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}