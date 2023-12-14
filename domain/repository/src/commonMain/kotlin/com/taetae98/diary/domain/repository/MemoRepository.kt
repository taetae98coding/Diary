package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.account.Memo

public interface MemoRepository {
    public suspend fun upsert(memo: Memo)
}
