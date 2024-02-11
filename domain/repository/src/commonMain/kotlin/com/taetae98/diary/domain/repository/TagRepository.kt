package com.taetae98.diary.domain.repository

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.tag.Tag
import kotlinx.coroutines.flow.Flow

public interface TagRepository {
    public suspend fun upsert(tag: Tag)
    public fun page(ownerId: String?): Flow<PagingData<Tag>>
    public fun find(tagId: String): Flow<Tag?>
}