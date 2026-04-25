package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.RoutineRemoteEntity

public fun RoutineLocalEntity.toRemote(): RoutineRemoteEntity {
    return RoutineRemoteEntity(
        id = id,
        detail = detail.toRemote(),
        rRules = rRules.map { it.toRemote() },
        rDates = rDates,
        exDates = exDates,
        isFinished = isFinished,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )
}

public fun RoutineRemoteEntity.toLocal(): RoutineLocalEntity {
    return RoutineLocalEntity(
        id = id,
        detail = detail.toLocal(),
        rRules = rRules.map { it.toLocal() },
        rDates = rDates,
        exDates = exDates,
        isFinished = isFinished,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )
}
