package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.database.api.datasource.SyncRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.mapper.toRemote
import io.github.taetae98coding.diary.core.network.api.datasource.RoutineRemoteDataSource
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class RoutineUploader(
    @param:Provided
    private val syncRoutineLocalDataSource: SyncRoutineLocalDataSource,
    @param:Provided
    private val routineRemoteDataSource: RoutineRemoteDataSource,
) : Uploader() {
    override suspend fun upload(accountId: Uuid) {
        push(
            getPending = { syncRoutineLocalDataSource.getBySyncState(accountId, SyncStateLocalEntity.PENDING) },
            toRemote = RoutineLocalEntity::toRemote,
            pushRemote = routineRemoteDataSource::push,
            markSynced = { list -> syncRoutineLocalDataSource.upsert(list.map { SyncRoutineLocalEntity(it.id, SyncStateLocalEntity.SYNCING) }) },
        )
    }
}
