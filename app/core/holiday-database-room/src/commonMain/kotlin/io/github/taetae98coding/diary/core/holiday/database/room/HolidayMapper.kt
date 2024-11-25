package io.github.taetae98coding.diary.core.holiday.database.room

import io.github.taetae98coding.diary.core.model.holiday.Holiday

internal fun Holiday.toEntity(): HolidayEntity =
	HolidayEntity(
		name = name,
		start = start,
		endInclusive = endInclusive,
	)

internal fun HolidayEntity.toHoliday(): Holiday =
	Holiday(
		name = name,
		start = start,
		endInclusive = endInclusive,
	)
