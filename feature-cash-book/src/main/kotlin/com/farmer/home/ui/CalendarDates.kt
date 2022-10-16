package com.farmer.home.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.farmer.home.ui.states.CalendarUiState

val WeekDays = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
fun CalendarDates(uiState: CalendarUiState) {
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
                                spend = it.sumOfSpend
                            )
                        }
                    }
                }
            }
        }
    )
}
