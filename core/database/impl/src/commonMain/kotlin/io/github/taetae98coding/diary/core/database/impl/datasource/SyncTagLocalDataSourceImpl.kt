package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.SyncTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
internal class SyncTagLocalDataSourceImpl(private val database: DiaryDatabase) : SyncTagLocalDataSource {
    override suspend fun upsert(entity: SyncTagLocalEntity) {
        database.syncTagDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<SyncTagLocalEntity>) {
        database.syncTagDao().upsert(entities)
    }

    override suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<TagLocalEntity> {
        return database.syncTagDao().getBySyncState(accountId, state)
    }

    override suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long {
        return database.syncTagDao().getLastUpdatedAt(accountId, state)
    }
}
