package com.taetae98.diary.library.compose.calendar.month

import androidx.compose.runtime.Immutable
import com.taetae98.diary.library.compose.calendar.week.WeekState
import kotlinx.datetime.Month

@Immutable
public data class MonthState(
    val year: Int,
    val month: Month,
) {
    internal val weekState = List(6) { WeekState(year, month, it) }
}