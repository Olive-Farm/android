package com.farmer.feature_statistics

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import com.patrykandpatrick.vico.core.component.shape.Shape
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farmer.feature_statistics.StatisticsUiState.ChartData
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.horizontalLegend
import com.patrykandpatrick.vico.compose.legend.legendItem
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultColors.Dark.axisGuidelineColor
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.legend.HorizontalLegend

import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale





@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
)
{
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.refreshStatic()
    var isCategoryClicked by remember { mutableStateOf(true) }
    var isIntegrationClicked by remember { mutableStateOf(false) }


        if (uiState.chartDataList.isNullOrEmpty()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF7F7F7)),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.Center,
            ){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                    contentAlignment = Alignment.Center)
                {
                    Spacer(modifier = Modifier.height(10.dp)) // Spacer 추가
                    Text(
                        text = "소비내역 분석 리포트",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                    )
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                    contentAlignment = Alignment.Center)
                {
                    SelectDate(
                        year = uiState.year,
                        month = uiState.month,
                        onNewDateSelect = viewModel::onNewDateSelect
                    )
                }

                Box(modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp, top = 10.dp, bottom = 5.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF7F7F7),
                        shape = RoundedCornerShape(size = 15.dp)
                    ),
                    contentAlignment = Alignment.Center)
                {

                    ColorChangingButtons(
                        isCategoryClicked = isCategoryClicked,
                        isIntegrationClicked = isIntegrationClicked,
                        onCategoryClicked = { isCategoryClicked = true; isIntegrationClicked = false },
                        onIntegrationClicked = { isIntegrationClicked = true; isCategoryClicked = false }
                    )

                }

                Box(
                    Modifier
                        .fillMaxSize() // 화면 너비에 맞게 넓힘
                        .padding(start = 25.dp, end = 25.dp, top = 10.dp, bottom = 15.dp)
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(size = 15.dp)
                        )
                        .padding(
                            start = 30.dp,
                            end = 30.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Column(){
                        Text(
                            text = "입력한 내역이 없습니다!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF355A1E)
                                    )
                                ) {
                                    append("OliveBook")
                                }
                                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                                    append("을 이용하여\n 편리하게 가계부를 관리해보세요")
                                }
                            }, fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                }




                }
        }

        else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF7F7F7)),
                //horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {

                item{
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFFF7F7F7)),
                        //horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ){
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                            contentAlignment = Alignment.Center)
                        {
                            Spacer(modifier = Modifier.height(10.dp)) // Spacer 추가
                            Text(
                                text = "소비내역 분석 리포트 \uD83D\uDCA1",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF000000),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                            )
                        }

                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                            contentAlignment = Alignment.Center)
                        {
                            SelectDate(
                                year = uiState.year,
                                month = uiState.month,
                                onNewDateSelect = viewModel::onNewDateSelect
                            )
                        }

                        Box(modifier = Modifier
                            .padding(start = 25.dp, end = 25.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFEDEBE8),
                                shape = RoundedCornerShape(size = 15.dp)
                            ),
                            contentAlignment = Alignment.Center)
                        {

                            ColorChangingButtons(
                                isCategoryClicked = isCategoryClicked,
                                isIntegrationClicked = isIntegrationClicked,
                                onCategoryClicked = { isCategoryClicked = true; isIntegrationClicked = false },
                                onIntegrationClicked = { isIntegrationClicked = true; isCategoryClicked = false }
                            )
                        }
                    }
                }


                item {
                    Box(
                        Modifier
                            .fillMaxWidth() // 화면 너비에 맞게 넓힘
                            .padding(start = 25.dp, end = 25.dp, top = 10.dp, bottom = 15.dp)
                            .background(
                                color = Color(0xFFFFFFFF),
                                shape = RoundedCornerShape(size = 15.dp)
                            )
                            .padding(
                                start = 30.dp,
                                end = 30.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                    ) {
                        Spacer(modifier = Modifier.height(8.dp)) // Spacer 추가

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 40.dp),
                                text = "수입",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF626262),
                                textAlign = TextAlign.Right
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                modifier = Modifier
                                    .padding(end = 40.dp),
                                text = "지출",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF626262),
                                textAlign = TextAlign.Center
                            )
                        }

                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            GetTotalIncome(totalIncome = uiState.totalIncome)
                            Spacer(modifier = Modifier.weight(1f))

                            GetTotalPrice(totalPrice = uiState.totalPrice)
                        }

                    }
                }

                if (isCategoryClicked) {
                    item {
                        Text(
                            text = "카테고리별 지출",
                            fontSize = 20.sp,
                            fontWeight = FontWeight(700),
                            textAlign = TextAlign.Start,
                            color = Color(0xFF626262),
                            modifier = Modifier.padding(
                                start = 25.dp,
                                end = 25.dp,
                                top = 20.dp,
                                bottom = 10.dp
                            )
                        )
                    }

                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 25.dp, end = 25.dp, bottom = 20.dp)
                                .background(
                                    color = Color(0xFFFFFFFF),
                                    shape = RoundedCornerShape(size = 15.dp)
                                )
                                .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                            ) {
                                PieChart(
                                    modifier = Modifier
                                        .height(280.dp)
                                        .padding(top = 20.dp, bottom = 20.dp),
                                    pieChartData = PieChartData(uiState.chartDataList.map { it.toPieChartData() }),
                                    animation = simpleChartAnimation(),
                                    sliceDrawer = SimpleSliceDrawer(50f)
                                )
                                uiState.chartDataList.forEach { chartData ->
                                    ChartDataCategoryItem(chartData)
                                }
                            }
                        }
                    }


                    item {
                        Text(
                            text = "이번 달 코멘트 \uD83D\uDCE2",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight(700),
                            color = Color(0xFF626262),
                            modifier = Modifier.padding(
                                start = 25.dp,
                                end = 25.dp,
                                top = 20.dp,
                                bottom = 10.dp
                            )
                        )
                    }
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth() // 화면 너비에 맞게 넓힘
                                .padding(start = 25.dp, end = 25.dp, bottom = 20.dp)
                                .background(
                                    color = Color(0xFFFFFFFF),
                                    shape = RoundedCornerShape(size = 15.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(bottom = 5.dp)) {
                                Row(
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(start = 20.dp, top = 10.dp, end = 10.dp),
                                        text = "지난 달보다",
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight(600),
                                        color = Color(0xFF626262),
                                        textAlign = TextAlign.Left,
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    compareExpenses(uiState.lastPrice, uiState.totalPrice)
                                }


                                Row(
                                    modifier = Modifier.padding(top = 5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                )
                                {
                                    Text(
                                        modifier = Modifier
                                            .padding(start = 20.dp, end = 15.dp),
                                        text = "가장 많이 쓴 내역은",
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight(600),
                                        color = Color(0xFF626262),
                                        textAlign = TextAlign.Left,
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    CategoryTextWithMaxCount(chartDataList = uiState.chartDataList)
                                }
                            }

                        }
                    }
                }

                else if(isIntegrationClicked){

                    item {
                        Text(
                            text = "월간 내역",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight(700),
                            color = Color(0xFF626262),
                            modifier = Modifier.padding(
                                start = 25.dp,
                                end = 25.dp,
                                top = 20.dp,
                                bottom = 10.dp
                            )
                        )
                    }

                    item {
                        Box(
                            Modifier
                                .fillMaxWidth() // 화면 너비에 맞게 넓힘
                                .padding(start = 25.dp, end = 25.dp, bottom = 20.dp)
                                .background(
                                    color = Color(0xFFFFFFFF),
                                    shape = RoundedCornerShape(size = 15.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier.padding(top = 15.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            )
                            {
                                val incomeList = uiState.incomeList.map { it.incomePrice }
                                val spendList = uiState.spendList.map { it.spendPrice }
                                ComposedChart(
                                    columnChartColors = listOf(Color(0xff6E90C4), Color(0xffEB7257)),
                                    completedIncomeList = incomeList, // 수입
                                    completedSpendList = spendList // 지출
                                )
                                IncomeSpendItem()
                                Log.e("@@수입 내역 확인", "$incomeList")
                                Log.e("@@지출 내역 확인", "$spendList")
                            }
                        }
                    }
                }


            }
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
            .clickable {
                timePickerDialog.show()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier,
                //.padding(start = 10.dp),
            text = "${year}년 ${month}월 ",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,

        )
        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
        //Spacer(modifier = Modifier.padding(horizontal = 14.dp)) // Spacer 추가
    }

}

@Composable
fun IncomeSpendItem() {

    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {

        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(Color(0xff6E90C4))
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "수입",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            textAlign = TextAlign.Left
        )
        //Spacer(modifier = Modifier.weight(0.5f))
        Box(
            modifier = Modifier
                .size(20.dp)
        )
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background( Color(0xffEB7257))
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "지출",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Left
        )

    }
}

@Composable
fun ChartDataCategoryItem(chartData: ChartData) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(chartData.color)
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = chartData.categoryName,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Left
            )

            Text(
                text = " (${chartData.percentage.toInt()}%)",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left
            )
        Spacer(modifier = Modifier.weight(1f))

        val formattedNumber = "%,d".format(chartData.spend)

            Text(
                text = "${formattedNumber}원",
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Right
            )

        Spacer(
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
        )
    }
}


@Composable
fun ColorChangingButtons(
    isCategoryClicked: Boolean,
    isIntegrationClicked: Boolean,
    onCategoryClicked: () -> Unit,
    onIntegrationClicked: () -> Unit
) {
    //var isCategoryClicked by remember { mutableStateOf(true) }
    //var isIntegrationClicked by remember { mutableStateOf(false) }

    val backgroundColorCategory = if (isCategoryClicked) Color(0xFF94D0af) else Color(0xFFEDEBE8)
    val textColorCategory = if (isCategoryClicked) Color.White else Color(0xFF626262)

    val backgroundColorIntegration =
        if (isIntegrationClicked)  Color(0xFF94D0af) else Color(0xFFEDEBE8) // 배경
    val textColorIntegration = if (isIntegrationClicked) Color.White else Color(0xFF626262)

    // 카테고리 버튼을 클릭했을 때, 월간 내역 버튼 상태 업데이트
    val onClickCategory: () -> Unit = {
        if (!isCategoryClicked) {
            onCategoryClicked()
        }
    }

    // 월간 내역 버튼을 클릭했을 때, 카테고리 버튼 상태 업데이트
    val onClickIntegration: () -> Unit = {
        if (!isIntegrationClicked) {
            onIntegrationClicked()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(IntrinsicSize.Min)
                .padding(start = 3.dp, end = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onClickCategory, // 카테고리 버튼에 대한 클릭 핸들러 할당
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backgroundColorCategory,
                    contentColor = textColorCategory
                ),
                shape = RoundedCornerShape(30), // 조절 가능한 모서리 반지름
                border = BorderStroke(0.dp, Color.Transparent), // 테두리 제거
                elevation = null, // 그림자 제거
                modifier = Modifier
                    .padding(start = 2.dp, end = 2.dp)
                    .fillMaxWidth()

            ) {
                Text(
                    text = "카테고리",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(IntrinsicSize.Min)
                .padding(start = 3.dp, end = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onClickIntegration, // 월간 내역 버튼에 대한 클릭 핸들러 할당
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backgroundColorIntegration,
                    contentColor = textColorIntegration
                ),
                shape = RoundedCornerShape(30), // 조절 가능한 모서리 반지름
                border = BorderStroke(0.dp, Color.Transparent), // 테두리 제거
                elevation = null, // 그림자 제거
                modifier = Modifier
                    .padding(start = 2.dp, end = 2.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "월간 내역",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




@Composable
fun GetTotalPrice(totalPrice: Int) {
    val formattedPrice = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalPrice)
    Text(
        text = buildAnnotatedString {
            withStyle(
                style =
                SpanStyle(
                    fontSize = 19.sp,
                    color = Color(0xFFEB7257),
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append("$formattedPrice 원")
            }
        },
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 30.dp)
    )
}
@Composable
fun GetTotalIncome(totalIncome: Int) {
    val formattedPrice = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalIncome)
    Text(
        text = buildAnnotatedString {
            withStyle(
                style =
                SpanStyle(
                    fontSize = 19.sp,
                    color = Color(0xFF6E90C4),
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append("$formattedPrice 원")
            }
        },
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 30.dp)
    )
}


@Composable
fun CategoryTextWithMaxCount(
    chartDataList: List<ChartData>,
    viewModel: StatisticsViewModel = hiltViewModel()) {
    val categoryWithMaxCount = viewModel.findCategoryWithMaxCount(chartDataList)

    Text(
        modifier = Modifier
            .padding(start = 20.dp, end = 15.dp),
        text = categoryWithMaxCount,
        fontSize = 19.sp,
        color = Color(0xFFFF9A01),
        fontWeight =FontWeight.SemiBold,
        textAlign = TextAlign.Right

    )

}

@Composable
fun compareExpenses(lastPrice:Int, totalPrice: Int) {

    val difference = totalPrice - lastPrice

    val formattedDifference = "%,d원".format(difference)

    Text(
        modifier = Modifier
            .padding(start = 20.dp, end = 15.dp, top = 10.dp,),
        text = if (difference > 0) "+${formattedDifference}" else formattedDifference,
        fontSize = 19.sp,
        color = Color(0xFF799E81),
        fontWeight =FontWeight.SemiBold,
        textAlign = TextAlign.Right

    )
}

@Composable
fun rememberChartStyle(columnChartColors: List<Color>): ChartStyle {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    return remember(columnChartColors, isSystemInDarkTheme) {
        val defaultColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light

        ChartStyle(
            axis = ChartStyle.Axis(
                axisLabelColor = Color(defaultColors.axisLabelColor),
               axisGuidelineColor = Color( 0x00ffffff), // 축 색깔 설정 나중에 바꾸기
                axisLineColor = Color(defaultColors.axisLineColor)
            ),
            columnChart = ChartStyle.ColumnChart(
                columns = columnChartColors.map { columnColor ->
                    LineComponent(
                        color = columnColor.toArgb(),
                        thicknessDp = 25f,
                        shape = com.patrykandpatrick.vico.core.component.shape.Shapes.roundedCornerShape(topRightPercent = 20, topLeftPercent = 20)
                    )
                },
                dataLabel = TextComponent.Builder().build()
            ),
            lineChart = ChartStyle.LineChart(lines = emptyList()),
            marker = ChartStyle.Marker(),
            elevationOverlayColor = Color(defaultColors.elevationOverlayColor)
        )
    }
}

//@Composable
//fun rememberLegend(colors: List<Color>): HorizontalLegend {
//    val labelTextList = listOf("수입", "지출")
//
//    return horizontalLegend(
//        items = List(labelTextList.size) { index ->
//            legendItem(
//                icon = shapeComponent(
//                    shape = com.patrykandpatrick.vico.core.component.shape.Shapes.pillShape,
//                    color = colors[index]
//                ),
//                label = textComponent(),
//                labelText = labelTextList[index]
//            )
//        },
//        iconSize = 10.dp,
//        iconPadding = 8.dp,
//        spacing = 10.dp,
//        padding = dimensionsOf(top = 8.dp),
//    )
//}

private fun intListAsFloatEntryList(list: List<Int>): List<FloatEntry> {
    val floatEntryList = arrayListOf<FloatEntry>()
    floatEntryList.clear()

    list.forEachIndexed { index, item ->
        floatEntryList.add(entryOf(x = index.toFloat(), y = item.toFloat()))
    }

    return floatEntryList
}

@Composable
fun ComposedChart(columnChartColors: List<Color>, completedIncomeList: List<Int>, completedSpendList: List<Int>, viewModel: StatisticsViewModel = hiltViewModel()) {
    val completedPlanEntry = ChartEntryModelProducer(intListAsFloatEntryList(completedIncomeList))
    val completedRateEntry = ChartEntryModelProducer(intListAsFloatEntryList(completedSpendList))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colorList = columnChartColors

    ProvideChartStyle(rememberChartStyle(columnChartColors = colorList)) {
        val completedPlanChart = columnChart(
            mergeMode = ColumnChart.MergeMode.Grouped,
            spacing = 25.dp
        )
        val completedRateChart = columnChart(
            mergeMode = ColumnChart.MergeMode.Grouped,
            spacing = 25.dp
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Chart(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp),
                chart = remember(completedPlanChart, completedRateChart) {
                    completedPlanChart + completedRateChart
                },
               // legend = rememberLegend(colors = colorList),
                chartModelProducer = ComposedChartEntryModelProducer(completedPlanEntry.plus(completedRateEntry)),
                //startAxis = rememberStartAxis(
                // itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = maxYRange / 10 + 1)
                // ),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = { value, _ ->
                        val month = uiState.month - value.toInt()
                        ("${month}월")
                    }
                ),
                runInitialAnimation = true,
                chartScrollState = rememberChartScrollState()
            )
        }

    }
}





