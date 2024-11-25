package io.github.taetae98coding.diary.library.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

public fun LocalDate.Companion.todayIn(
	clock: Clock = Clock.System,
	timeZone: TimeZone = TimeZone.currentSystemDefault(),
): LocalDate = clock.todayIn(timeZone)

public fun LocalDate.toTimeInMillis(): Long = atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

public fun Long.toLocalDate(): LocalDate =
	Instant
		.fromEpochMilliseconds(this)
		.toLocalDateTime(TimeZone.UTC)
		.date

public operator fun LocalDate.Companion.invoke(year: Int, month: Month, weekOfMonth: Int, dayOfWeek: DayOfWeek): LocalDate {
	val date = LocalDate(year, month, 1)

	return date
		.minus(date.dayOfWeek.christ, DateTimeUnit.DAY)
		.plus(weekOfMonth, DateTimeUnit.WEEK)
		.plus(dayOfWeek.christ, DateTimeUnit.DAY)
}

public fun ClosedRange<LocalDate>.isOverlap(range: ClosedRange<LocalDate>): Boolean = start <= range.endInclusive && endInclusive >= range.start
