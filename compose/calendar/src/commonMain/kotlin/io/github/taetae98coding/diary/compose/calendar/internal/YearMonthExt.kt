package io.github.taetae98coding.diary.compose.calendar.internal

import kotlinx.datetime.YearMonth
import kotlinx.datetime.number

internal fun Int.toYearMonth(): YearMonth {
    return YearMonth(
        year = this / 12,
        month = this % 12 + 1,
    )
}

internal fun YearMonth.toPage(): Int {
    return year * 12 + month.number - 1
}
