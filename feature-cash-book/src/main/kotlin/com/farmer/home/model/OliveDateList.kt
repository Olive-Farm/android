package com.farmer.home.model

import androidx.compose.ui.graphics.Color
import com.farmer.home.util.StringKeySet.FRI
import com.farmer.home.util.StringKeySet.MON
import com.farmer.home.util.StringKeySet.SAT
import com.farmer.home.util.StringKeySet.SUN
import com.farmer.home.util.StringKeySet.THU
import com.farmer.home.util.StringKeySet.TUE
import com.farmer.home.util.StringKeySet.WED
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY

internal object OliveDateList {
    val list = listOf(
        OliveDate(
            dayOfTheWeek = SUNDAY,
            shortDayOfTheWeek = SUN,
            textColor = Color.Red
        ),
        OliveDate(
            dayOfTheWeek = MONDAY,
            shortDayOfTheWeek = MON
        ),
        OliveDate(
            dayOfTheWeek = TUESDAY,
            shortDayOfTheWeek = TUE
        ),
        OliveDate(
            dayOfTheWeek = WEDNESDAY,
            shortDayOfTheWeek = WED
        ),
        OliveDate(
            dayOfTheWeek = THURSDAY,
            shortDayOfTheWeek = THU
        ),
        OliveDate(
            dayOfTheWeek = FRIDAY,
            shortDayOfTheWeek = FRI
        ),
        OliveDate(
            dayOfTheWeek = SATURDAY,
            shortDayOfTheWeek = SAT,
            textColor = Color.Blue
        )
    )
}
