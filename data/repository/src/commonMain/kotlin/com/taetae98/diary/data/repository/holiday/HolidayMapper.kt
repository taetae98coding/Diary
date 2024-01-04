package com.taetae98.diary.data.repository.holiday

import com.taetae98.diary.data.dto.holiday.HolidayDto
import com.taetae98.diary.domain.entity.holiday.Holiday

internal fun HolidayDto.toDomain(): Holiday {
    return Holiday(
        name = name,
        start = start,
        endInclusive = endInclusive,
    )
}