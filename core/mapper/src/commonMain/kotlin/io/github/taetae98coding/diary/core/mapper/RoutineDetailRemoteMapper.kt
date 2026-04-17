package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RoutineDetailLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.RoutineDetailRemoteEntity

public fun RoutineDetailLocalEntity.toRemote(): RoutineDetailRemoteEntity {
    return RoutineDetailRemoteEntity(
        title = title,
        description = description,
        start = start,
        endInclusive = endInclusive,
        color = color,
        routineCount = routineCount,
    )
}

public fun RoutineDetailRemoteEntity.toLocal(): RoutineDetailLocalEntity {
    return RoutineDetailLocalEntity(
        title = title,
        description = description,
        start = start,
        endInclusive = endInclusive,
        color = color,
        routineCount = routineCount,
    )
}
