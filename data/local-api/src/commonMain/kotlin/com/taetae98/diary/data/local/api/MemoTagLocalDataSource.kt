package com.taetae98.diary.data.local.api

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.memo.MemoTagDto
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

public interface MemoTagLocalDataSource {
    public suspend fun exists(memoTag: MemoTagDto): Boolean
    public suspend fun delete(memoTag: MemoTagDto)
    public suspend fun upsert(memoTag: MemoTagDto)
    public suspend fun upsert(memoTag: List<MemoTagDto>)
    public suspend fun afterAt(ownerId: String, updateAt: Instant?, limit: Long): List<MemoDto>

    public fun findByMemoId(memoId: String): Flow<List<MemoTagDto>>
}
