package com.taetae98.diary.library.calendar.compose.month

import com.taetae98.diary.library.calendar.compose.week.WeekState
import kotlinx.datetime.Month

public data class MonthState(
    val year: Int,
    val month: Month,
) {
    internal val weekState = List(6) { WeekState(year, month, it) }
}