package com.taetae98.diary.data.local.api

import com.taetae98.diary.data.dto.memo.MemoTagDto

public interface MemoTagLocalDataSource {
    public suspend fun exists(memoTag: MemoTagDto): Boolean
    public suspend fun delete(memoTag: MemoTagDto)
    public suspend fun upsert(memoTag: MemoTagDto)
}
