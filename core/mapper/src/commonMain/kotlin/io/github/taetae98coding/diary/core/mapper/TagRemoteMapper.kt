package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.TagRemoteEntity

public fun TagLocalEntity.toRemote(): TagRemoteEntity {
    return TagRemoteEntity(
        id = id,
        detail = detail.toRemote(),
        isFinished = isFinished,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )
}

public fun TagRemoteEntity.toLocal(): TagLocalEntity {
    return TagLocalEntity(
        id = id,
        detail = detail.toLocal(),
        isFinished = isFinished,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )
}
