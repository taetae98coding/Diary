package com.taetae98.diary.library.compose.calendar.week

import androidx.compose.runtime.Immutable
import com.taetae98.diary.library.kotlin.ext.toChristDayNumber
import com.taetae98.diary.library.kotlin.ext.toChristDayOfWeek
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Immutable
public data class WeekState(
    val year: Int,
    val month: Month,
    val weekOfMonth: Int,
) {
    internal val start = LocalDate(year, month, 1)
        .let { it.minus(it.dayOfWeek.toChristDayNumber(), DateTimeUnit.DAY) }
        .plus(weekOfMonth, DateTimeUnit.WEEK)

    internal val endInclusive = start.plus(6, DateTimeUnit.DAY)

    internal val weekDayState = List(7) {
        WeekDayState(
            year = year,
            month = month,
            weekOfMonth = weekOfMonth,
            dayOfWeek = it.toChristDayOfWeek()
        )
    }
}
