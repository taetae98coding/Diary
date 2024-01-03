package com.taetae98.diary.data.remote.impl.holiday

import com.taetae98.diary.data.dto.holiday.HolidayDto
import com.taetae98.diary.data.remote.impl.holiday.entity.HolidayEntity

internal fun HolidayEntity.toDto(): HolidayDto {
    val date = getLocalDate()

    return HolidayDto(
        name = name.toPrettyName(),
        start = date,
        endInclusive = date
    )
}

private fun String.toPrettyName(): String {
    return when(this) {
        "1월1일" -> "새해"
        "기독탄신일" -> "크리스마스"
        else -> this
    }
}