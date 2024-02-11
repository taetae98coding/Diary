package com.taetae98.diary.data.repository.memo

import app.cash.paging.PagingData
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.library.paging.mapPaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class MemoRepositoryImpl(
    private val fireStore: MemoFireStore,
    private val prefDataSource: MemoPrefDataSource,
    private val localDataSource: MemoLocalDataSource,
    @Named(CoroutinesModule.PROCESS)
    private val processScope: CoroutineScope,
) : MemoRepository {
    override suspend fun upsert(memo: Memo) {
        val dto = memo.toDto()

        localDataSource.upsert(dto)
        runOnProcessScopeIfOwnerIdNotNull(dto) { fireStore.upsert(dto) }
    }

    override suspend fun updateFinish(id: String, isFinished: Boolean) {
        val memo = localDataSource.find(id).firstOrNull()

        localDataSource.updateFinish(id, isFinished)
        runOnProcessScopeIfOwnerIdNotNull(memo) { fireStore.updateFinished(id, isFinished) }
    }

    override suspend fun delete(id: String) {
        val memo = localDataSource.find(id).firstOrNull()

        localDataSource.delete(id)
        runOnProcessScopeIfOwnerIdNotNull(memo) { fireStore.delete(id) }
    }

    private fun runOnProcessScopeIfOwnerIdNotNull(memo: MemoDto?, run: suspend () -> Unit) {
        if (memo?.ownerId == null) return

        processScope.launch {
            runCatching { run() }
        }
    }

    override suspend fun fetch(uid: String) {
        while (true) {
            val updateAt = prefDataSource.getFetchedUpdateAt(uid).firstOrNull()
            val data = fireStore.pageByUpdateAt(uid, updateAt).takeIf { it.isNotEmpty() } ?: break

            data.forEach {
                if (it.isDeleted) {
                    localDataSource.delete(it.id)
                } else {
                    localDataSource.upsert(it)
                }
            }

            prefDataSource.setFetchedUpdateAt(uid, data.last().updateAt)
        }
    }

    override fun find(id: String): Flow<Memo?> {
        return localDataSource.find(id)
            .map { it?.toDomain() }
    }

    override fun find(ownerId: String?, dateRange: ClosedRange<LocalDate>): Flow<List<Memo>> {
        return localDataSource.find(ownerId, dateRange)
            .map { it.map(MemoDto::toDomain) }
    }

    override fun page(ownerId: String?): Flow<PagingData<Memo>> {
        return createPager(
            config = createPagingConfig(
                pageSize = 30,
            ),
            pagingSourceFactory = {
                localDataSource.page(ownerId = ownerId)
            }
        ).mapPaging(MemoDto::toDomain)
    }

    override fun page(ownerId: String?, tagId: String): Flow<PagingData<Memo>> {
        return createPager(
            config = createPagingConfig(
                pageSize = 30,
            ),
            pagingSourceFactory = {
                localDataSource.page(ownerId, tagId)
            }
        ).mapPaging(MemoDto::toDomain)
    }
}
