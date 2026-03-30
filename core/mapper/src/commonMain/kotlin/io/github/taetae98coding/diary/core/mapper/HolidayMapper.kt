package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.holiday.database.api.entity.HolidayLocalEntity
import io.github.taetae98coding.diary.core.holiday.network.api.entity.HolidayRemoteEntity
import io.github.taetae98coding.diary.core.model.holiday.Holiday

public fun HolidayLocalEntity.toDomain(): Holiday {
    return Holiday(
        name = name,
        isHoliday = isHoliday,
        localDateRange = start..endInclusive,
    )
}

public fun HolidayRemoteEntity.toLocal(): HolidayLocalEntity {
    return HolidayLocalEntity(
        name = name,
        isHoliday = isHoliday,
        start = start,
        endInclusive = endInclusive,
    )
}
