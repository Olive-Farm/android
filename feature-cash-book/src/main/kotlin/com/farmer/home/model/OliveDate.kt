package com.farmer.home.model

import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.DayOfWeek

internal data class OliveDate(
    val dayOfTheWeek: DayOfWeek,
    val shortDayOfTheWeek: String,
    @ColorRes val textColor: Color = Color.DarkGray
)