package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.memo.MemoTag

public interface MemoTagFireStoreRepository {
    public suspend fun delete(memoTag: MemoTag)
    public suspend fun upsert(memoTag: MemoTag)
    public suspend fun fetch(uid: String)
    public suspend fun upsert(memoTag: List<MemoTag>)
}