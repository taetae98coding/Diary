package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.memo.Memo

public interface MemoFireStoreRepository {
    public suspend fun upsert(memo: Memo)
    public suspend fun updateFinish(id: String, isFinish: Boolean)
}
