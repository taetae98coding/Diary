package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.AccountTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.MemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.MemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.TagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.AccountTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.mapper.toRemote
import io.github.taetae98coding.diary.core.network.api.datasource.MemoRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.datasource.MemoTagRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.datasource.TagRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.MemoRemoteEntity
import io.github.taetae98coding.diary.core.network.api.entity.MemoTagRemoteEntity
import io.github.taetae98coding.diary.core.network.api.entity.TagRemoteEntity
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class Synchronizer(
    @param:Provided
    private val databaseTransactor: DatabaseTransactor,
    @param:Provided
    private val memoLocalDataSource: MemoLocalDataSource,
    @param:Provided
    private val accountMemoLocalDataSource: AccountMemoLocalDataSource,
    @param:Provided
    private val syncMemoLocalDataSource: SyncMemoLocalDataSource,
    @param:Provided
    private val memoRemoteDataSource: MemoRemoteDataSource,
    @param:Provided
    private val memoTagLocalDataSource: MemoTagLocalDataSource,
    @param:Provided
    private val syncMemoTagLocalDataSource: SyncMemoTagLocalDataSource,
    @param:Provided
    private val memoTagRemoteDataSource: MemoTagRemoteDataSource,
    @param:Provided
    private val tagLocalDataSource: TagLocalDataSource,
    @param:Provided
    private val accountTagLocalDataSource: AccountTagLocalDataSource,
    @param:Provided
    private val syncTagLocalDataSource: SyncTagLocalDataSource,
    @param:Provided
    private val tagRemoteDataSource: TagRemoteDataSource,
) {
    suspend fun sync(accountId: Uuid) {
        pushTags(accountId)
        pushMemos(accountId)
        pushMemoTags(accountId)
        pullTags(accountId)
        pullMemos(accountId)
        pullMemoTags(accountId)
    }

    private suspend fun <Local, Remote> push(
        getPending: suspend () -> List<Local>,
        toRemote: (Local) -> Remote,
        pushRemote: suspend (List<Remote>) -> Unit,
        markSyncing: suspend (List<Local>) -> Unit,
    ) {
        while (true) {
            val pending = getPending()
            if (pending.isEmpty()) break

            pushRemote(pending.map(toRemote))
            markSyncing(pending)
        }
    }

    private suspend fun <Remote> pull(
        getLastUpdatedAt: suspend () -> Long,
        pullRemote: suspend (Long) -> List<Remote>,
        save: suspend (List<Remote>) -> Unit,
    ) {
        while (true) {
            val updatedAt = getLastUpdatedAt()
            val remoteList = pullRemote(updatedAt)
            if (remoteList.isEmpty()) break

            databaseTransactor.writeTransaction { save(remoteList) }
        }
    }

    private suspend fun pushMemos(accountId: Uuid) {
        push(
            getPending = { syncMemoLocalDataSource.getBySyncState(accountId, SyncStateLocalEntity.PENDING) },
            toRemote = MemoLocalEntity::toRemote,
            pushRemote = memoRemoteDataSource::push,
            markSyncing = { list -> syncMemoLocalDataSource.upsert(list.map { SyncMemoLocalEntity(it.id, SyncStateLocalEntity.SYNCING) }) },
        )
    }

    private suspend fun pullMemos(accountId: Uuid) {
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

    private suspend fun pushTags(accountId: Uuid) {
        push(
            getPending = { syncTagLocalDataSource.getBySyncState(accountId, SyncStateLocalEntity.PENDING) },
            toRemote = TagLocalEntity::toRemote,
            pushRemote = tagRemoteDataSource::push,
            markSyncing = { list -> syncTagLocalDataSource.upsert(list.map { SyncTagLocalEntity(it.id, SyncStateLocalEntity.SYNCING) }) },
        )
    }

    private suspend fun pullTags(accountId: Uuid) {
        pull(
            getLastUpdatedAt = { syncTagLocalDataSource.getLastUpdatedAt(accountId, SyncStateLocalEntity.UP_TO_DATE) },
            pullRemote = tagRemoteDataSource::pull,
            save = { list ->
                tagLocalDataSource.upsert(list.map(TagRemoteEntity::toLocal))
                accountTagLocalDataSource.upsert(list.map { AccountTagLocalEntity(accountId, it.id) })
                syncTagLocalDataSource.upsert(list.map { SyncTagLocalEntity(tagId = it.id, state = SyncStateLocalEntity.UP_TO_DATE) })
            },
        )
    }

    private suspend fun pushMemoTags(accountId: Uuid) {
        push(
            getPending = { syncMemoTagLocalDataSource.getBySyncState(accountId, SyncStateLocalEntity.PENDING) },
            toRemote = MemoTagLocalEntity::toRemote,
            pushRemote = memoTagRemoteDataSource::push,
            markSyncing = { list -> syncMemoTagLocalDataSource.upsert(list.map { SyncMemoTagLocalEntity(it.memoId, it.tagId, SyncStateLocalEntity.SYNCING) }) },
        )
    }

    private suspend fun pullMemoTags(accountId: Uuid) {
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
