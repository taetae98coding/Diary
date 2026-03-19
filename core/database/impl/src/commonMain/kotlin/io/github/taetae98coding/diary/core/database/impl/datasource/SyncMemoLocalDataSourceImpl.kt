package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
internal class SyncMemoLocalDataSourceImpl(private val database: DiaryDatabase) : SyncMemoLocalDataSource {
    override suspend fun upsert(entity: SyncMemoLocalEntity) {
        database.syncMemoDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<SyncMemoLocalEntity>) {
        database.syncMemoDao().upsert(entities)
    }

    override suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<MemoLocalEntity> {
        return database.syncMemoDao().getBySyncState(accountId, state)
    }

    override suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long {
        return database.syncMemoDao().getLastUpdatedAt(accountId, state)
    }
}
