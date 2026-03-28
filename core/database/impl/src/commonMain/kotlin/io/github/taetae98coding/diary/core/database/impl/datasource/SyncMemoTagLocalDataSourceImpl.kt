package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
internal class SyncMemoTagLocalDataSourceImpl(private val database: DiaryDatabase) : SyncMemoTagLocalDataSource {
    override suspend fun upsert(entity: SyncMemoTagLocalEntity) {
        database.syncMemoTagDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<SyncMemoTagLocalEntity>) {
        database.syncMemoTagDao().upsert(entities)
    }

    override suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<MemoTagLocalEntity> {
        return database.syncMemoTagDao().getBySyncState(accountId, state)
    }

    override suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long {
        return database.syncMemoTagDao().getLastUpdatedAt(accountId, state)
    }
}
