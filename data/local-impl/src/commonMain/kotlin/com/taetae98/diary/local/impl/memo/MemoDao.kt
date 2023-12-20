package com.taetae98.diary.local.impl.memo

import com.taetae98.diary.data.dto.memo.MemoDto

internal interface MemoDao {
    suspend fun upsert(memo: MemoDto)
}