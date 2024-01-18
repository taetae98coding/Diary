package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.tag.Tag

public interface TagRepository {
    public suspend fun upsert(tag: Tag)
}