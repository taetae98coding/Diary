package com.taetae98.diary.library.calendar.compose.ext

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber

private const val FIRST_YEAR = 1900
private const val LAST_YEAR = 2100

internal const val CALENDAR_PAGE_SIZE = (LAST_YEAR - FIRST_YEAR + 1) * 12

internal fun LocalDate.toPage(): Int {
    val yearPage = (year - FIRST_YEAR) * 12
    val monthPage = month.ordinal

    return yearPage + monthPage
}

internal fun Int.pageToLocalDate(): LocalDate {
    val year = this / 12 + FIRST_YEAR
    val month = this % 12 + 1

    return LocalDate(year, month, 1)
}

internal fun Int.toChristDayOfWeek(): DayOfWeek {
    return when (this) {
        0 -> DayOfWeek.SUNDAY
        else -> DayOfWeek(this)
    }
}

internal fun DayOfWeek.toChristDayNumber(): Int {
    return when(this) {
        DayOfWeek.SUNDAY -> 0
        else -> isoDayNumber
    }
}
