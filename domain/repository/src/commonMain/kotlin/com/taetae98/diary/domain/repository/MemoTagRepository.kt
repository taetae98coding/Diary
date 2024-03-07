package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.memo.MemoTag
import kotlinx.coroutines.flow.Flow

public interface MemoTagRepository {
    public suspend fun delete(memoTag: MemoTag)
    public suspend fun upsert(memoTag: MemoTag)
    public suspend fun upsert(memoTag: List<MemoTag>)
    public suspend fun fetch(uid: String)

    public fun findByMemoId(memoId: String): Flow<List<MemoTag>>
}