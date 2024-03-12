package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.tag.Tag

public interface TagFireStoreRepository {
    public suspend fun upsert(tag: Tag)
    public suspend fun delete(tagId: String)
    public suspend fun fetch(uid: String)

}