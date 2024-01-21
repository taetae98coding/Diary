package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.memo.MemoTag

public interface MemoTagRepository {
    public suspend fun exists(memoTag: MemoTag): Boolean
    public suspend fun delete(memoTag: MemoTag): Boolean
    public suspend fun upsert(memoTag: MemoTag): Boolean
}