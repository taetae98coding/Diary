package com.taetae98.diary.library.kotlin.ext

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime

public fun localDateNow(): LocalDate {
    return getLocalDateTimeNow().date
}

public fun LocalDate.toEpochMilliseconds(): Long {
    return atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
}

public fun Long.toLocalDate(): LocalDate {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC).date
}

public fun Int.toChristDayOfWeek(): DayOfWeek {
    return when (this % 7) {
        0 -> DayOfWeek.SUNDAY
        else -> DayOfWeek(this)
    }
}

public fun DayOfWeek.toChristDayNumber(): Int {
    return when (this) {
        DayOfWeek.SUNDAY -> 0
        else -> isoDayNumber
    }
}
