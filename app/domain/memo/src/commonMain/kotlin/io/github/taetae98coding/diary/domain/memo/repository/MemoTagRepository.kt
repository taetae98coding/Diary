package io.github.taetae98coding.diary.domain.memo.repository

import io.github.taetae98coding.diary.core.model.tag.Tag
import kotlinx.coroutines.flow.Flow

public interface MemoTagRepository {
    public suspend fun upsert(memoId: String, tagId: String)
    public suspend fun delete(memoId: String, tagId: String)

    public fun findByMemoId(memoId: String): Flow<List<Tag>>
}
