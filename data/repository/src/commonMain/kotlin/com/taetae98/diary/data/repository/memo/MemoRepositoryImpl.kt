package com.taetae98.diary.data.repository.memo

import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.domain.entity.account.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoRepositoryImpl(
    private val localDataSource: MemoLocalDataSource,
) : MemoRepository {
    override suspend fun upsert(memo: Memo) {
        localDataSource.upsert(memo.toDto())
    }

    override fun page(): Flow<PagingData<Memo>> {
        val pager = createPager<Int, MemoDto>(
            config = createPagingConfig(
                pageSize = 30,
                initialLoadSize = 30,
                jumpThreshold = 60
            ),
            pagingSourceFactory = {
                localDataSource.page()
            }
        )

        TODO("Not yet implemented")
    }
}
