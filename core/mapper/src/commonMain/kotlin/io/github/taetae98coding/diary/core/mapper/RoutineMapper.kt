package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.model.routine.Routine

public fun RoutineLocalEntity.toDomain(): Routine {
    return Routine(
        id = id,
        detail = detail.toDomain(),
        rRules = rRules.map { it.toDomain() },
        rDates = rDates.toSet(),
        exDates = exDates.toSet(),
        isCalendarVisible = isCalendarVisible,
        isFinished = isFinished,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )
}
