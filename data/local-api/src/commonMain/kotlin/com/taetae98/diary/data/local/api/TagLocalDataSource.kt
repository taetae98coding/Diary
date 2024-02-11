package com.taetae98.diary.data.local.api

import app.cash.paging.PagingSource
import com.taetae98.diary.data.dto.tag.TagDto
import kotlinx.coroutines.flow.Flow

public interface TagLocalDataSource {
    public suspend fun upsert(tag: TagDto)
    public suspend fun delete(id: String)
    public fun page(ownerId: String?): PagingSource<Int, TagDto>
    public fun find(id: String): Flow<TagDto?>
}