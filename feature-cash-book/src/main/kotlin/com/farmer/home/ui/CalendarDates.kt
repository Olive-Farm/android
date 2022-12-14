package com.farmer.home.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.home.model.OliveDateList
import com.farmer.home.ui.detail.DetailDialogByState
import com.farmer.home.ui.states.CalendarUiState
import com.farmer.home.ui.states.CalendarViewModel

@Composable
fun CalendarDates(
    uiState: CalendarUiState,
    viewModel: CalendarViewModel = hiltViewModel()
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
            if (uiState is CalendarUiState.CalendarState) {
                with(uiState) {
                    items(dateList) {
                        if (it.dateOfMonth != 0) {
                            CalendarDate(
                                date = it.dateOfMonth.toString(),
                                income = it.sumOfIncome,
                                spend = it.sumOfSpend,
                                onClick = {
                                    viewModel.setDetailDialogState(
                                        shouldShow = true,
                                        clickedDateOfMonth = it.dateOfMonth
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    )

    DetailDialogByState(uiState, viewModel)
}
