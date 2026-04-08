package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import kotlin.uuid.Uuid

public interface SyncRoutineLocalDataSource {
    public suspend fun upsert(entity: SyncRoutineLocalEntity)
    public suspend fun upsert(entities: Collection<SyncRoutineLocalEntity>)

    public suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<RoutineLocalEntity>

    public suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long
}
