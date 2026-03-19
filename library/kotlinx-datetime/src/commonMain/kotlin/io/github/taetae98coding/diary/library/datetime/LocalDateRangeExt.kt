package io.github.taetae98coding.diary.library.datetime

import kotlinx.datetime.LocalDateRange

public infix fun LocalDateRange.overlaps(other: LocalDateRange): Boolean {
    return start <= other.endInclusive && other.start <= endInclusive
}
