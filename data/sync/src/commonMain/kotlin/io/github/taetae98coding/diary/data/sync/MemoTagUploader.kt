package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.mapper.toRemote
import io.github.taetae98coding.diary.core.network.api.datasource.MemoTagRemoteDataSource
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class MemoTagUploader(
    @param:Provided
    private val syncMemoTagLocalDataSource: SyncMemoTagLocalDataSource,
    @param:Provided
    private val memoTagRemoteDataSource: MemoTagRemoteDataSource,
) : Uploader() {
    override suspend fun upload(accountId: Uuid) {
        push(
            getPending = { syncMemoTagLocalDataSource.getBySyncState(accountId, SyncStateLocalEntity.PENDING) },
            toRemote = MemoTagLocalEntity::toRemote,
            pushRemote = memoTagRemoteDataSource::push,
            markSynced = { list -> syncMemoTagLocalDataSource.upsert(list.map { SyncMemoTagLocalEntity(it.memoId, it.tagId, SyncStateLocalEntity.SYNCING) }) },
        )
    }
}
