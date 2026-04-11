package io.github.taetae98coding.diary.compose.calendar.internal

import io.github.taetae98coding.diary.library.datetime.toSundayBasedNumber
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Suppress("FunctionName")
public fun WeekLocalDateRange(
    yearMonth: YearMonth,
    weekOfMonth: Int,
): LocalDateRange {
    val start = yearMonth.firstDay
        .minus(yearMonth.firstDay.dayOfWeek.toSundayBasedNumber(), DateTimeUnit.DAY)
        .plus(weekOfMonth, DateTimeUnit.WEEK)
    val endInclusive = start.plus(6, DateTimeUnit.DAY)

    return LocalDateRange(start, endInclusive)
}
