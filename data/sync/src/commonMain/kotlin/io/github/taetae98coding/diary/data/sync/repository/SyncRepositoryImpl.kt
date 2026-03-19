package io.github.taetae98coding.diary.data.sync.repository

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.MemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.mapper.toRemote
import io.github.taetae98coding.diary.core.network.api.datasource.MemoRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.MemoRemoteEntity
import io.github.taetae98coding.diary.domain.sync.repository.SyncRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
public class SyncRepositoryImpl(
    private val coroutineScope: CoroutineScope,
    private val databaseTransactor: DatabaseTransactor,
    private val memoLocalDataSource: MemoLocalDataSource,
    private val accountMemoLocalDataSource: AccountMemoLocalDataSource,
    private val syncMemoLocalDataSource: SyncMemoLocalDataSource,
    private val memoRemoteDataSource: MemoRemoteDataSource,
) : SyncRepository {
    override suspend fun sync(accountId: Uuid) {
        pushMemos(accountId)
        pullMemos(accountId)
    }

    override fun requestSync(accountId: Uuid) {
        coroutineScope.launch {
            runCatching { sync(accountId) }
        }
    }

    private suspend fun pushMemos(accountId: Uuid) {
        while (true) {
            val localEntity = syncMemoLocalDataSource.getBySyncState(accountId, SyncStateLocalEntity.PENDING)
            if (localEntity.isEmpty()) {
                break
            }

            memoRemoteDataSource.push(localEntity.map(MemoLocalEntity::toRemote))
            syncMemoLocalDataSource.upsert(localEntity.map { SyncMemoLocalEntity(it.id, SyncStateLocalEntity.SYNCING) })
        }
    }

    private suspend fun pullMemos(accountId: Uuid) {
        while (true) {
            val updatedAt = syncMemoLocalDataSource.getLastUpdatedAt(accountId, SyncStateLocalEntity.UP_TO_DATE)
            val remoteEntity = memoRemoteDataSource.pull(updatedAt)
            if (remoteEntity.isEmpty()) {
                break
            }

            databaseTransactor.writeTransaction {
                memoLocalDataSource.upsert(remoteEntity.map(MemoRemoteEntity::toLocal))
                accountMemoLocalDataSource.upsert(remoteEntity.map { AccountMemoLocalEntity(accountId, it.id) })
                syncMemoLocalDataSource.upsert(remoteEntity.map { SyncMemoLocalEntity(memoId = it.id, state = SyncStateLocalEntity.UP_TO_DATE) })
            }
        }
    }
}
