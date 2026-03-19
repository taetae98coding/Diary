package io.github.taetae98coding.diary.compose.calendar.internal

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

internal fun Int.toSundayBasedDayOfWeek(): DayOfWeek {
    return when (this % 7) {
        0 -> DayOfWeek.SUNDAY
        else -> DayOfWeek(this)
    }
}

internal fun DayOfWeek.toSundayBasedNumber(): Int {
    return when (this) {
        DayOfWeek.SUNDAY -> 0
        else -> isoDayNumber
    }
}
