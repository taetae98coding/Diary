package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.domain.entity.account.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import org.koin.core.annotation.Factory

@Factory
internal class MemoRepositoryImpl : MemoRepository {
    override suspend fun upsert(memo: Memo) {
        TODO("Not yet implemented")
    }
}