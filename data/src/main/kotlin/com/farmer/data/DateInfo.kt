package com.farmer.data

import kotlinx.datetime.LocalDate

data class DateInfo(
    val date: LocalDate,
    val history: History?
)
