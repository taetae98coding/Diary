package com.taetae98.diary.data.local.api

import app.cash.paging.PagingSource
import com.taetae98.diary.data.dto.memo.MemoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

public interface MemoLocalDataSource {
    public suspend fun upsert(memo: MemoDto)
    public suspend fun fetch(memo: List<MemoDto>)
    public suspend fun complete(id: String)
    public suspend fun incomplete(id: String)
    public suspend fun delete(id: String)
    public fun find(id: String): Flow<MemoDto?>
    public fun find(ownerId: String?, dateRange: ClosedRange<LocalDate>): Flow<List<MemoDto>>
    public fun page(ownerId: String?): PagingSource<Int, MemoDto>
}
