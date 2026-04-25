package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.RoutineDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface RoutineLocalDataSource {
    public suspend fun upsert(entity: RoutineLocalEntity)

    public suspend fun upsert(entities: Collection<RoutineLocalEntity>)

    public suspend fun updateDetail(
        routineId: Uuid,
        detail: RoutineDetailLocalEntity,
        updatedAt: Long,
    )

    public suspend fun updateFinish(
        routineId: Uuid,
        isFinished: Boolean,
        updatedAt: Long,
    )

    public suspend fun updateDelete(
        routineId: Uuid,
        isDeleted: Boolean,
        updatedAt: Long,
    )

    public fun get(routineId: Uuid): Flow<RoutineLocalEntity?>
}
