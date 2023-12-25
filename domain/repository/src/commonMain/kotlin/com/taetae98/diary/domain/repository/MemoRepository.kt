package com.taetae98.diary.domain.repository

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.account.memo.Memo
import kotlinx.coroutines.flow.Flow

public interface MemoRepository {
    public suspend fun upsert(memo: Memo)
    public suspend fun finish(id: String)
    public suspend fun delete(id: String)

    public fun page(): Flow<PagingData<Memo>>
}
