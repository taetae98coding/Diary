package com.taetae98.diary.data.repository.memo.tag

import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.data.local.api.MemoTagLocalDataSource
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.library.kotlin.ext.mapCollectionLatest
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoTagRepositoryImpl(
    private val localDataSource: MemoTagLocalDataSource,
) : MemoTagRepository {
    override suspend fun exists(memoTag: MemoTag): Boolean {
        return localDataSource.exists(memoTag.toDto())
    }

    override suspend fun delete(memoTag: MemoTag) {
        localDataSource.delete(memoTag.toDto())
    }

    override suspend fun upsert(memoTag: MemoTag) {
        localDataSource.upsert(memoTag.toDto())
    }

    override fun findByMemoId(memoId: String): Flow<List<MemoTag>> {
        return localDataSource.findByMemoId(memoId)
            .mapCollectionLatest(MemoTagDto::toDomain)
    }
}
