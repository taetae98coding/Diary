package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RoutineDetailLocalEntity
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail

public fun RoutineDetail.toLocal(): RoutineDetailLocalEntity {
    return RoutineDetailLocalEntity(
        title = title,
        description = description,
        start = start,
        endInclusive = endInclusive,
        color = color,
        routineCount = routineCount,
    )
}

public fun RoutineDetailLocalEntity.toDomain(): RoutineDetail {
    return RoutineDetail(
        title = title,
        description = description,
        start = start,
        endInclusive = endInclusive,
        color = color,
        routineCount = routineCount,
    )
}
