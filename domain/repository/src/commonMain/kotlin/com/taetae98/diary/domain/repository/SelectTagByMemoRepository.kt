package com.taetae98.diary.domain.repository

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.tag.Tag
import kotlinx.coroutines.flow.Flow

public interface SelectTagByMemoRepository {
    public fun find(ownerId: String?): Flow<List<Tag>>
    public fun page(ownerId: String?): Flow<PagingData<Memo>>

    public suspend fun upsert(tagId: String)
    public suspend fun delete(tagId: String)
}
