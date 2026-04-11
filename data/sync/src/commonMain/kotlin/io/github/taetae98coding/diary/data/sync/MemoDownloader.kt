package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.MemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.network.api.datasource.MemoRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.MemoRemoteEntity
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class MemoDownloader(
    @Provided
    databaseTransactor: DatabaseTransactor,
    @param:Provided
    private val memoLocalDataSource: MemoLocalDataSource,
    @param:Provided
    private val accountMemoLocalDataSource: AccountMemoLocalDataSource,
    @param:Provided
    private val syncMemoLocalDataSource: SyncMemoLocalDataSource,
    @param:Provided
    private val memoRemoteDataSource: MemoRemoteDataSource,
) : Downloader(databaseTransactor) {
    override suspend fun download(accountId: Uuid) {
        pull(
            getLastUpdatedAt = { syncMemoLocalDataSource.getLastUpdatedAt(accountId, SyncStateLocalEntity.UP_TO_DATE) },
            pullRemote = memoRemoteDataSource::pull,
            save = { list ->
                memoLocalDataSource.upsert(list.map(MemoRemoteEntity::toLocal))
                accountMemoLocalDataSource.upsert(list.map { AccountMemoLocalEntity(accountId, it.id) })
                syncMemoLocalDataSource.upsert(list.map { SyncMemoLocalEntity(memoId = it.id, state = SyncStateLocalEntity.UP_TO_DATE) })
            },
        )
    }
}
