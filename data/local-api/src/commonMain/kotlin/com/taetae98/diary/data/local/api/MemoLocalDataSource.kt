package com.taetae98.diary.data.local.api

import com.taetae98.diary.data.dto.memo.MemoDto

public interface MemoLocalDataSource {
    public suspend fun upsert(memo: MemoDto)
}
