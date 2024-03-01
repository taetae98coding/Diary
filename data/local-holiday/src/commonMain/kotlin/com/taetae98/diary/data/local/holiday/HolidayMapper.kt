package com.taetae98.diary.data.local.holiday

import com.taetae98.diary.data.dto.holiday.HolidayDto
import kotlinx.datetime.LocalDate

internal fun HolidayDto.toEntity(): HolidayEntity {
    return HolidayEntity(
        name = name,
        startAt = start,
        endAt = endInclusive,
    )
}

internal fun mapToHolidayDto(name: String, startAt: LocalDate, endAt: LocalDate): HolidayDto {
    return HolidayDto(
        name = name,
        start = startAt,
        endInclusive = endAt,
    )
}