package com.farmer.home.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.home.model.OliveDateList
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

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
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(7),
            content = {
                items(OliveDateList.list) {
                    Text(
                        text = it.shortDayOfTheWeek,
                        fontWeight = FontWeight.Normal,
                        color = it.textColor
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
