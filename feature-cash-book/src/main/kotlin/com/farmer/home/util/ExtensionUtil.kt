package com.farmer.home.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import java.time.Month.APRIL
import java.time.Month.AUGUST
import java.time.Month.DECEMBER
import java.time.Month.FEBRUARY
import java.time.Month.JANUARY
import java.time.Month.JULY
import java.time.Month.JUNE
import java.time.Month.MARCH
import java.time.Month.MAY
import java.time.Month.NOVEMBER
import java.time.Month.OCTOBER
import java.time.Month.SEPTEMBER

internal val String.Companion.EMPTY: String get() = ""

internal fun Int.toCommaString(): String =
    this.toString().reversed().chunked(3).joinToString(",").reversed()

internal fun String.toKotlinDateTimeMonth(): Month =
    when (this.toIntOrNull() ?: -1) {
        1 -> JANUARY
        2 -> FEBRUARY
        3 -> MARCH
        4 -> APRIL
        5 -> MAY
        6 -> JUNE
        7 -> JULY
        8 -> AUGUST
        9 -> SEPTEMBER
        10 -> OCTOBER
        11 -> NOVEMBER
        12 -> DECEMBER
        else -> JANUARY
    }

internal fun String.toKotlinDayOfWeek(): DayOfWeek =
    when (this) {
        "월" -> DayOfWeek.MONDAY
        "화" -> DayOfWeek.TUESDAY
        "수" -> DayOfWeek.WEDNESDAY
        "목" -> DayOfWeek.THURSDAY
        "금" -> DayOfWeek.FRIDAY
        "토" -> DayOfWeek.SATURDAY
        "일" -> DayOfWeek.SUNDAY
        else -> DayOfWeek.MONDAY
    }