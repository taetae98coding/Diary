package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RoutineDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.RoutineRemoteEntity
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

public fun RoutineLocalEntity.toRemote(): RoutineRemoteEntity {
    return RoutineRemoteEntity(
        id = id,
        title = detail.title,
        description = detail.description,
        start = detail.start?.toString(),
        endInclusive = detail.endInclusive?.toString(),
        color = detail.color,
        rRules = Json.encodeToString(rRules),
        rDates = Json.encodeToString(rDates.map { it.toString() }),
        exDates = Json.encodeToString(exDates.map { it.toString() }),
        routineCount = detail.routineCount,
        isFinished = isFinished,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )
}

public fun RoutineRemoteEntity.toLocal(): RoutineLocalEntity {
    return RoutineLocalEntity(
        id = id,
        detail = RoutineDetailLocalEntity(
            title = title,
            description = description,
            start = start?.let(LocalDate::parse),
            endInclusive = endInclusive?.let(LocalDate::parse),
            color = color,
            routineCount = routineCount,
        ),
        rRules = Json.decodeFromString(rRules),
        rDates = Json.decodeFromString<List<String>>(rDates).map(LocalDate::parse),
        exDates = Json.decodeFromString<List<String>>(exDates).map(LocalDate::parse),
        isFinished = isFinished,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )
}
