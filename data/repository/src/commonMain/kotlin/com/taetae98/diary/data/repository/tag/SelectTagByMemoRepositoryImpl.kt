package com.taetae98.diary.data.repository.tag

import app.cash.paging.PagingData
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.api.SelectTagByMemoLocalDataSource
import com.taetae98.diary.data.repository.memo.toDomain
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.repository.SelectTagByMemoRepository
import com.taetae98.diary.library.kotlin.ext.mapCollectionLatest
import com.taetae98.diary.library.paging.mapPaging
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class SelectTagByMemoRepositoryImpl(
    private val localDataSource: SelectTagByMemoLocalDataSource,
) : SelectTagByMemoRepository {
    override fun find(ownerId: String?): Flow<List<Tag>> {
        return localDataSource.find(ownerId)
            .mapCollectionLatest(TagDto::toDomain)
    }

    override fun page(ownerId: String?): Flow<PagingData<Memo>> {
        return createPager(
            config = createPagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { localDataSource.page(ownerId) }
        ).mapPaging(MemoDto::toDomain)
    }

    override suspend fun upsert(tagId: String) {
        localDataSource.upsert(tagId)
    }

    override suspend fun delete(tagId: String) {
        localDataSource.delete(tagId)
    }


    companion object {
        private const val PAGE_SIZE = 30
    }
}