package com.taetae98.diary.data.local.api

import com.taetae98.diary.data.dto.tag.TagDto

public interface TagLocalDataSource {
    public suspend fun upsert(tag: TagDto)
}