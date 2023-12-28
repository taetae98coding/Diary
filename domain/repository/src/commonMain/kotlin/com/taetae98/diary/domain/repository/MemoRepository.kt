package com.taetae98.diary.domain.repository

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.account.memo.Memo
import kotlinx.coroutines.flow.Flow

public interface MemoRepository {
    public suspend fun upsert(memo: Memo)
    public suspend fun complete(id: String)
    public suspend fun incomplete(id: String)
    public suspend fun delete(id: String)
    public suspend fun fetch()
    public fun find(id: String): Flow<Memo?>
    public fun page(ownerId: String?): Flow<PagingData<Memo>>
}
