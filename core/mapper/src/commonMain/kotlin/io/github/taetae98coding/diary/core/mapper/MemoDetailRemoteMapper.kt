package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.MemoDetailLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.MemoDetailRemoteEntity

public fun MemoDetailLocalEntity.toRemote(): MemoDetailRemoteEntity {
    return MemoDetailRemoteEntity(
        title = title,
        description = description,
        isAllDay = isAllDay,
        start = start,
        endInclusive = endInclusive,
        color = color,
    )
}

public fun MemoDetailRemoteEntity.toLocal(): MemoDetailLocalEntity {
    return MemoDetailLocalEntity(
        title = title,
        description = description,
        isAllDay = isAllDay,
        start = start,
        endInclusive = endInclusive,
        color = color,
    )
}
