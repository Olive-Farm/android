package com.farmer.home.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.home.model.OliveDateList
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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

            }
        )

        }
    }
}
