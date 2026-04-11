package io.github.taetae98coding.diary.library.datetime

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

public fun DayOfWeek.isWeekend(): Boolean {
    return this == DayOfWeek.SATURDAY || this == DayOfWeek.SUNDAY
}

public fun Int.toSundayBasedDayOfWeek(): DayOfWeek {
    return when (this % 7) {
        0 -> DayOfWeek.SUNDAY
        else -> DayOfWeek(this)
    }
}

public fun DayOfWeek.toSundayBasedNumber(): Int {
    return when (this) {
        DayOfWeek.SUNDAY -> 0
        else -> isoDayNumber
    }
}
