package com.farmer.home.ui.states

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month


/**
 * @param year ex) ... 2021, 2022, 2023 ...
 * @param month
 * @param dateOfMonth ex) 1, 2, 3 ... 31
 * @param dayOfWeek
 * @param sumOfIncome sum of the income on the date
 * @param sumOfSpend sum of the spend on the date
 * @param incomeList income list on the date
 * @param spendList spend list on the date
 */
data class DateUiInfo(
    val year: Int,
    val month: Month,
    val dateOfMonth: Int,
    val dayOfWeek: DayOfWeek,
    val sumOfIncome: Int,
    val sumOfSpend: Int,
    val incomeList: List<Int>,
    val spendList: List<Int>,
    val isClickedDate: Boolean = false
) {
    companion object {
        val EMPTY = DateUiInfo(
            year = -1,
            month = Month.JANUARY,
            dateOfMonth = 0,
            dayOfWeek = DayOfWeek.MONDAY,
            sumOfIncome = 0,
            sumOfSpend = 0,
            incomeList = emptyList(),
            spendList = emptyList(),
            isClickedDate = false
        )
    }
}
