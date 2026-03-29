package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.MemoRemoteEntity

public fun MemoLocalEntity.toRemote(): MemoRemoteEntity {
    return MemoRemoteEntity(
        id = id,
        detail = detail.toRemote(),
        primaryTag = primaryTag,
        isFinished = isFinished,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )
}

public fun MemoRemoteEntity.toLocal(): MemoLocalEntity {
    return MemoLocalEntity(
        id = id,
        detail = detail.toLocal(),
        primaryTag = primaryTag,
        isFinished = isFinished,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )
}
