package com.taetae98.diary.data.local.api

import app.cash.paging.PagingSource
import com.taetae98.diary.data.dto.memo.MemoDto

public interface MemoLocalDataSource {
    public suspend fun upsert(memo: MemoDto)
    public suspend fun finish(id: String)
    public suspend fun delete(id: String)
    public fun page(ownerId: String?): PagingSource<Int, MemoDto>
}
