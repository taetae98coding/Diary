package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import kotlin.uuid.Uuid

public interface SyncTagLocalDataSource {
    public suspend fun upsert(entity: SyncTagLocalEntity)
    public suspend fun upsert(entities: Collection<SyncTagLocalEntity>)

    public suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<TagLocalEntity>

    public suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long
}
