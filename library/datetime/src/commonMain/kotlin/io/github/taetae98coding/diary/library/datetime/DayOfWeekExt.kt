package io.github.taetae98coding.diary.library.datetime

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

public fun Int.toChristDayOfWeek(): DayOfWeek {
    return if (this % 7 == 0) {
        DayOfWeek.SUNDAY
    } else {
        DayOfWeek(this)
    }
}

public val DayOfWeek.christ: Int
    get() = isoDayNumber % 7
