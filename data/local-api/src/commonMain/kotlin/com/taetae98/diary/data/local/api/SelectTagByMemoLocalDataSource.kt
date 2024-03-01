package com.taetae98.diary.data.local.api

import app.cash.paging.PagingSource
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.tag.TagDto
import kotlinx.coroutines.flow.Flow

public interface SelectTagByMemoLocalDataSource {
    public fun find(ownerId: String?): Flow<List<TagDto>>
    public fun page(ownerId: String?): PagingSource<Int, MemoDto>

    public suspend fun upsert(tagId: String)
    public suspend fun delete(tagId: String)
}