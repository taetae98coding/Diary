package io.github.taetae98coding.diary.library.datetime

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.plus

public infix fun LocalDateRange.overlaps(other: LocalDateRange): Boolean {
    return start <= other.endInclusive && other.start <= endInclusive
}

public fun List<LocalDate>.toLocalDateRanges(): List<LocalDateRange> {
    return asSequence()
        .distinct()
        .sorted()
        .fold(emptyList()) { ranges, date ->
            val last = ranges.lastOrNull()
            if (last != null && date == last.endInclusive.plus(1, DateTimeUnit.DAY)) {
                ranges.dropLast(1) + listOf(last.start..date)
            } else {
                ranges + listOf(date..date)
            }
        }
}
