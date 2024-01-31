package com.taetae98.diary.data.repository.memo.tag

import com.taetae98.diary.data.local.api.MemoTagLocalDataSource
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
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
}
