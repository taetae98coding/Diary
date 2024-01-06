package com.taetae98.diary.library.compose.calendar.week

import androidx.compose.runtime.Immutable
import com.taetae98.diary.library.kotlin.ext.toChristDayNumber
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Immutable
public data class WeekDayState(
    val year: Int,
    val month: Month,
    val weekOfMonth: Int,
    val dayOfWeek: DayOfWeek
) {
    public val localDate: LocalDate = run {
        val monthLocalDate = LocalDate(year, month, 1)

        monthLocalDate.plus(weekOfMonth, DateTimeUnit.WEEK)
            .minus(monthLocalDate.dayOfWeek.toChristDayNumber(), DateTimeUnit.DAY)
            .plus(dayOfWeek.toChristDayNumber(), DateTimeUnit.DAY)
    }

    public fun isSameMonth(): Boolean {
        return year == localDate.year && month == localDate.month
    }
}
