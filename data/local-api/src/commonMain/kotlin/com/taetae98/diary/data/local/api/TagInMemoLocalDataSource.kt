package com.taetae98.diary.data.local.api

import com.taetae98.diary.data.dto.tag.TagDto
import kotlinx.coroutines.flow.Flow

public interface TagInMemoLocalDataSource {
    public fun find(ownerId: String?): Flow<List<TagDto>>
}