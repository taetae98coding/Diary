package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.SyncRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
internal class SyncRoutineLocalDataSourceImpl(private val database: DiaryDatabase) : SyncRoutineLocalDataSource {
    override suspend fun upsert(entity: SyncRoutineLocalEntity) {
        database.syncRoutineDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<SyncRoutineLocalEntity>) {
        database.syncRoutineDao().upsert(entities)
    }

    override suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<RoutineLocalEntity> {
        return database.syncRoutineDao().getBySyncState(accountId, state)
    }

    override suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long {
        return database.syncRoutineDao().getLastUpdatedAt(accountId, state)
    }
}
