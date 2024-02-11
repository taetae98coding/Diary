package com.taetae98.diary.domain.repository

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.memo.Memo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

public interface MemoRepository {
    public suspend fun upsert(memo: Memo)
    public suspend fun updateFinish(id: String, isFinished: Boolean)
    public suspend fun delete(id: String)
    public suspend fun fetch(uid: String)
    public fun find(id: String): Flow<Memo?>
    public fun find(ownerId: String?, dateRange: ClosedRange<LocalDate>): Flow<List<Memo>>
    public fun page(ownerId: String?): Flow<PagingData<Memo>>
    public fun page(ownerId: String?, tagId: String): Flow<PagingData<Memo>>
}
