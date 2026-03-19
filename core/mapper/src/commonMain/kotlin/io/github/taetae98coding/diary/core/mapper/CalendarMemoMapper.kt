package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.CalendarMemoLocalEntity
import io.github.taetae98coding.diary.core.model.memo.CalendarMemo

public fun CalendarMemoLocalEntity.toDomain(): CalendarMemo {
    return CalendarMemo(
        id = id,
        title = title,
        isAllDay = isAllDay,
        localDateTimeRange = start..endInclusive,
        color = color,
    )
}
