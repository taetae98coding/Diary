package com.taetae98.diary.data.local.api

import com.taetae98.diary.data.dto.memo.MemoTagDto
import kotlinx.coroutines.flow.Flow

public interface MemoTagLocalDataSource {
    public suspend fun exists(memoTag: MemoTagDto): Boolean
    public suspend fun delete(memoTag: MemoTagDto)
    public suspend fun insert(memoTag: MemoTagDto)
    public suspend fun insert(memoTag: List<MemoTagDto>)

    public fun findByMemoId(memoId: String): Flow<List<MemoTagDto>>
}
