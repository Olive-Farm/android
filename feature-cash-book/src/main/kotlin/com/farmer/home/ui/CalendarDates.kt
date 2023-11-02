package com.farmer.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.home.model.OliveDateList
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.datetime.Month
import java.text.DecimalFormat
import java.time.DayOfWeek

@Composable
fun CalendarDates(
    uiState: CalendarUiState.Success,
    viewModel: TempCalendarViewModel = hiltViewModel()
) {
    val isRefreshing = viewModel.isRefreshing.collectAsState().value
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.refreshMonth()
        }
    ) {
        Column (modifier = Modifier.padding(all = 3.dp)){
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth()
                .padding(2.dp),

            columns = GridCells.Fixed(7),
            content = {
                items(OliveDateList.list) {     //요일
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = it.shortDayOfTheWeek,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        fontWeight = FontWeight(400),
                        color =
                            if (it.shortDayOfTheWeek == "일" || it.shortDayOfTheWeek == "토") Color(0xFF646464)
                            else Color(0xFFC2C2C2),
                    )
                }

                items(uiState.dateViewInfo) {
                    if (!it.isEmptyDate) {
                        it.dateInfo?.date?.dayOfMonth.toString()
                        it.dateInfo?.history?.spendList?.earnList?.sumOf { data -> data.price }
                        CalendarDate(
                            date = it.dateInfo?.date?.dayOfMonth.toString(),
                            income = it.dateInfo?.history?.spendList?.spendList?.sumOf { data ->
                                data.price
                            } ?: 0,
                            spend = it.dateInfo?.history?.spendList?.earnList?.sumOf { data ->
                                data.price
                            } ?: 0,
                            dayOfWeek = it.dateInfo?.date?.dayOfWeek,
                            year = it.dateInfo?.date?.year.toString(),
                            month = it.dateInfo?.date?.monthNumber.toString(),
                            onClick = {
                                viewModel.setShowDetailDialog(
                                    shouldShow = true,
                                    clickedDateInfo = it.dateInfo
                                )
                            }
                        )
                    }
                }

                item(span = {GridItemSpan(7)}) {
                    Column (modifier = Modifier.fillMaxWidth()
                        .padding(top = 10.dp)){
                        TransactionHistory(
                    month = uiState.dateViewInfo.last().dateInfo?.date?.month ?: Month.JANUARY,
                    totalIncome = uiState.totalIncome,
                    totalSpend = uiState.totalSpend
                )
                    }
                }

            }

        )

        }
    }
}

@Composable
private fun TransactionHistory(
    month: Month,
    totalIncome: Int,
    totalSpend: Int
) {
    val numFormat = DecimalFormat("#,###")

    Box(modifier = Modifier
        .padding(bottom = 50.dp)
        .fillMaxWidth()
        .height(127.dp)
        .background(color = Color(0xFFF6F2E5),
            shape = RoundedCornerShape(size = 12.dp)
        )
        .padding(all = 10.dp)
        .verticalScroll(rememberScrollState())) {
        Column {
            Text(
                modifier = Modifier.padding(5.dp),
                text = "이번달 소비 및 지출 내역",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.padding(horizontal = 7.dp)){/*
                Icon(
                    Icons.Filled.LocalAtm,
                    modifier = Modifier.padding(5.dp)
                        .align(Alignment.Top),
                    contentDescription = null,
                    tint = Color(0xFFEAB756)
                )*/
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFFEAB756), fontWeight = FontWeight.Bold, fontSize = 17.sp)){
                            append("${month.value}월 ")
                        }
                        append(" 총 지출 금액 : ")
                        withStyle(style = SpanStyle(color = RedAlpha200, fontWeight = FontWeight.Bold, fontSize = 17.sp)){
                            append("${numFormat.format(totalIncome)}원") }}
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.padding(horizontal = 7.dp)){/*
                Icon(
                    Icons.Filled.Savings,
                    modifier = Modifier.padding(5.dp)
                        .align(Alignment.Top),
                    contentDescription = null,
                    tint = Color(0x8092C88D)
                )*/
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFFEAB756), fontWeight = FontWeight.Bold, fontSize = 17.sp)){
                            append("${month.value}월 ")
                        }
                        append(" 총 수입 금액 : ")
                        withStyle(style = SpanStyle(color = BlueAlpha200, fontWeight = FontWeight.Bold, fontSize = 17.sp)){
                            append("${numFormat.format(totalSpend)}원") }}
                )
            }
        }
    }
}
