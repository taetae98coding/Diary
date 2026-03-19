package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import kotlin.uuid.Uuid

public interface SyncMemoLocalDataSource {
    public suspend fun upsert(entity: SyncMemoLocalEntity)
    public suspend fun upsert(entities: Collection<SyncMemoLocalEntity>)

    public suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<MemoLocalEntity>

    public suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long
}
