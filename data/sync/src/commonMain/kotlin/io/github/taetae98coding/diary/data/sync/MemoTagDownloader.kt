package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.MemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.network.api.datasource.MemoTagRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.MemoTagRemoteEntity
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class MemoTagDownloader(
    @Provided
    databaseTransactor: DatabaseTransactor,
    @param:Provided
    private val memoTagLocalDataSource: MemoTagLocalDataSource,
    @param:Provided
    private val syncMemoTagLocalDataSource: SyncMemoTagLocalDataSource,
    @param:Provided
    private val memoTagRemoteDataSource: MemoTagRemoteDataSource,
) : Downloader(databaseTransactor) {
    override suspend fun download(accountId: Uuid) {
        pull(
            getLastUpdatedAt = { syncMemoTagLocalDataSource.getLastUpdatedAt(accountId, SyncStateLocalEntity.UP_TO_DATE) },
            pullRemote = memoTagRemoteDataSource::pull,
            save = { list ->
                memoTagLocalDataSource.upsert(list.map(MemoTagRemoteEntity::toLocal))
                syncMemoTagLocalDataSource.upsert(list.map { SyncMemoTagLocalEntity(memoId = it.memoId, tagId = it.tagId, state = SyncStateLocalEntity.UP_TO_DATE) })
            },
        )
    }
}
