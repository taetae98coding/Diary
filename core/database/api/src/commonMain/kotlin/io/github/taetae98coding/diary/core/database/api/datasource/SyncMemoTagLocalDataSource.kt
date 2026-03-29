package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import kotlin.uuid.Uuid

public interface SyncMemoTagLocalDataSource {
    public suspend fun upsert(entity: SyncMemoTagLocalEntity)
    public suspend fun upsert(entities: Collection<SyncMemoTagLocalEntity>)

    public suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<MemoTagLocalEntity>

    public suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long
}
