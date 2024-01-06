package com.taetae98.diary.data.repository.memo

import app.cash.paging.PagingData
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.library.paging.mapPaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.koin.core.annotation.Factory

@Factory
internal class MemoRepositoryImpl(
    private val fireStore: MemoFireStore,
    private val prefDataSource: MemoPrefDataSource,
    private val localDataSource: MemoLocalDataSource,
) : MemoRepository {
    override suspend fun upsert(memo: Memo) {
        val dto = memo.toDto()

        fireStore.upsert(dto)
        localDataSource.upsert(dto)
    }

    override suspend fun complete(id: String) {
        fireStore.complete(id)
        localDataSource.complete(id)
    }

    override suspend fun incomplete(id: String) {
        fireStore.incomplete(id)
        localDataSource.incomplete(id)
    }

    override suspend fun delete(id: String) {
        fireStore.delete(id)
        localDataSource.delete(id)
    }

    override suspend fun fetch(uid: String) {
        while (true) {
            val updateAt = prefDataSource.getFetchedUpdateAt(uid).firstOrNull()
            val data = fireStore.pageByUpdateAt(uid, updateAt).takeIf { it.isNotEmpty() } ?: break

            prefDataSource.setFetchedUpdateAt(uid, data.last().updateAt)
            localDataSource.fetch(data)
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
}
