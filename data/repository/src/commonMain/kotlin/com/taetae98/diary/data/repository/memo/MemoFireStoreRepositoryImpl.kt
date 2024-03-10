package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoFireStoreRepository
import org.koin.core.annotation.Factory

@Factory
internal class MemoFireStoreRepositoryImpl : MemoFireStoreRepository{
    override suspend fun upsert(memo: Memo) {
        TODO("Not yet implemented")
    }
}