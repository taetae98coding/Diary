package com.taetae98.diary.local.impl.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.local.impl.memo.MemoDao

internal class EmptyMemoDao : MemoDao {
    override suspend fun upsert(memo: MemoDto) {

    }
}