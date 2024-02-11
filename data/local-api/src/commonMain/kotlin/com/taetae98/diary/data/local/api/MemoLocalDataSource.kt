package com.taetae98.diary.data.local.api

import app.cash.paging.PagingSource
import com.taetae98.diary.data.dto.memo.MemoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

public interface MemoLocalDataSource {
    public suspend fun upsert(memo: MemoDto)
    public suspend fun updateFinish(id: String, isFinished: Boolean)
    public suspend fun delete(id: String)
    public fun find(id: String): Flow<MemoDto?>
    public fun find(ownerId: String?, dateRange: ClosedRange<LocalDate>): Flow<List<MemoDto>>
    public fun page(ownerId: String?): PagingSource<Int, MemoDto>
    public fun page(ownerId: String?, tagId: String): PagingSource<Int, MemoDto>
}
