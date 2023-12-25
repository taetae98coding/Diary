package com.taetae98.diary.data.repository.memo

import app.cash.paging.PagingData
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.domain.entity.account.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.library.paging.mapPaging
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoRepositoryImpl(
    private val fireStore: MemoFireStore,
    private val localDataSource: MemoLocalDataSource,
) : MemoRepository {
    override suspend fun upsert(memo: Memo) {
        val dto = memo.toDto()

        fireStore.upsert(dto)
        localDataSource.upsert(dto)
    }

    override suspend fun finish(id: String) {
        fireStore.finish(id)
        localDataSource.finish(id)
    }

    override suspend fun delete(id: String) {
        fireStore.delete(id)
        localDataSource.delete(id)
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
