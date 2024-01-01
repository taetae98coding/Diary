package com.taetae98.diary.library.calendar.compose.week

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

public data class WeekDayState(
    val year: Int,
    val month: Month,
    val weekOfMonth: Int,
    val dayOfWeek: DayOfWeek
)
