package com.taetae98.diary.data.repository.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import org.koin.core.annotation.Factory

@Factory
internal class MemoTagRepositoryImpl : MemoTagRepository {
    override suspend fun exists(memoTag: MemoTag): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun delete(memoTag: MemoTag): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun upsert(memoTag: MemoTag): Boolean {
        TODO("Not yet implemented")
    }
}