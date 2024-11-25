package io.github.taetae98coding.diary.core.holiday.service.mapper

import io.github.taetae98coding.diary.core.holiday.service.entity.HolidayEntity
import io.github.taetae98coding.diary.core.model.holiday.Holiday

internal fun HolidayEntity.toHoliday(): Holiday =
	Holiday(
		name = name,
		start = localDate,
		endInclusive = localDate,
	)
