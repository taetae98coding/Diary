package com.taetae98.diary.library.compose.calendar.week

import com.taetae98.diary.library.compose.calendar.ext.toChristDayOfWeek
import kotlinx.datetime.Month

public data class WeekState(
    val year: Int,
    val month: Month,
    val weekOfMonth: Int,
) {
    internal val weekDayState = List(7) {
        WeekDayState(
            year = year,
            month = month,
            weekOfMonth = weekOfMonth,
            dayOfWeek = it.toChristDayOfWeek()
        )
    }
}
