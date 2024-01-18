package com.taetae98.diary.data.local.api

import app.cash.paging.PagingSource
import com.taetae98.diary.data.dto.tag.TagDto

public interface TagLocalDataSource {
    public suspend fun upsert(tag: TagDto)
    public fun page(ownerId: String?): PagingSource<Int, TagDto>

}