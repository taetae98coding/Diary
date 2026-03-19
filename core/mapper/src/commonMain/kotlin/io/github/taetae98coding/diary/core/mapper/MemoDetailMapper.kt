package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.MemoDetailLocalEntity
import io.github.taetae98coding.diary.core.model.memo.MemoDetail

public fun MemoDetail.toLocal(): MemoDetailLocalEntity {
    return MemoDetailLocalEntity(
        title = title,
        description = description,
        isAllDay = isAllDay,
        start = localDateTimeRange?.start,
        endInclusive = localDateTimeRange?.endInclusive,
        color = color,
    )
}

public fun MemoDetailLocalEntity.toDomain(): MemoDetail {
    val start = start
    val endInclusive = endInclusive

    return MemoDetail(
        title = title,
        description = description,
        isAllDay = isAllDay,
        localDateTimeRange = if (start == null || endInclusive == null) {
            null
        } else {
            start..endInclusive
        },
        color = color,
    )
}
