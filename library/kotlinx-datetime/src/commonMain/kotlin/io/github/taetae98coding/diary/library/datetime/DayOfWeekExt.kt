package io.github.taetae98coding.diary.library.datetime

import kotlinx.datetime.DayOfWeek

public fun DayOfWeek.isWeekend(): Boolean {
    return this == DayOfWeek.SATURDAY || this == DayOfWeek.SUNDAY
}
