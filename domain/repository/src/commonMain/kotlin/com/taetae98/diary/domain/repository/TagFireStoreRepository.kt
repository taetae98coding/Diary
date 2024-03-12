package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.tag.Tag

public interface TagFireStoreRepository {
    public suspend fun upsert(tag: Tag)
}