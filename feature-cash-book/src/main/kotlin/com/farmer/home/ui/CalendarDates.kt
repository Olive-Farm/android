package com.farmer.home.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.home.ui.detail.DetailDialog
import com.farmer.home.ui.states.CalendarUiState
import com.farmer.home.ui.states.CalendarViewModel

val WeekDays = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
fun CalendarDates(
    uiState: CalendarUiState,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(7),
        content = {
            items(WeekDays) {
                Text(
                    text = it,
                    fontWeight = FontWeight.Normal
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
                                    viewModel.setDetailDialogState()
                                }
                            )
                        }
                    }
                }

            }

        }
    )

    if (uiState is CalendarUiState.CalendarState && uiState.showDetailDialog) {
        Dialog(
            onDismissRequest = { viewModel.setDetailDialogState() }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 90.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                DetailDialog()
            }
        }
    }
}
