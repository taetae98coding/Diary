package io.github.taetae98coding.diary.compose.core.internal

import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

internal fun LocalDate.toEpochMillis(): Long {
    return atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
}

internal fun Long.toLocalDate(): LocalDate {
    return Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .date
}
