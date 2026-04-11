package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.mapper.toRemote
import io.github.taetae98coding.diary.core.network.api.datasource.MemoRemoteDataSource
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class MemoUploader(
    @param:Provided
    private val syncMemoLocalDataSource: SyncMemoLocalDataSource,
    @param:Provided
    private val memoRemoteDataSource: MemoRemoteDataSource,
) : Uploader() {
    override suspend fun upload(accountId: Uuid) {
        push(
            getPending = { syncMemoLocalDataSource.getBySyncState(accountId, SyncStateLocalEntity.PENDING) },
            toRemote = MemoLocalEntity::toRemote,
            pushRemote = memoRemoteDataSource::push,
            markSynced = { list -> syncMemoLocalDataSource.upsert(list.map { SyncMemoLocalEntity(it.id, SyncStateLocalEntity.SYNCING) }) },
        )
    }
}
