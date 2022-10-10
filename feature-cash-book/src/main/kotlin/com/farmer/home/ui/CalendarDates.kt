package com.farmer.home.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.toLocalDate

// todo move to viewmodel
// todo set korean
val WeekDays = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
fun CalendarDates(
    firstDayOfMonth: DayOfWeek,
    daysInMonth: Int,
    currentMonth: Month,
    currentYear: Int,
    currentDay: LocalDate
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
            items((getInitialDayOfMonth(firstDayOfMonth)..daysInMonth).toList()) {
                if (it > 0) {
                    val day = getGeneratedDay(it, currentMonth, currentYear)
                    val isCurrentDay = day == currentDay
                    // todo add plus, minus, average text in date
                    // todo need to chaneg to CalendarDate.kt
//                    Text(text = day.dayOfMonth.toString())
                    CalendarDate(
                        date = day.dayOfMonth.toString(),
                        income = "40000",
                        spend = "5000"
                    )
                }
            }
        }
    )
}

private fun getInitialDayOfMonth(firstDayOfMonth: DayOfWeek) = -(firstDayOfMonth.value).minus(2)

private fun getGeneratedDay(day: Int, currentMonth: Month, currentYear: Int): LocalDate {
    val monthValue =
        if (currentMonth.value.toString().length == 1) "0${currentMonth.value}" else currentMonth.value.toString()
    val newDay = if (day.toString().length == 1) "0$day" else day
    return "$currentYear-$monthValue-$newDay".toLocalDate()
}
