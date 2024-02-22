package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.tag.Tag
import kotlinx.coroutines.flow.Flow

public interface TagInMemoRepository {
    public fun find(ownerId: String?): Flow<List<Tag>>
}
