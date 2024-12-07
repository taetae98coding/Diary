package io.github.taetae98coding.diary.core.diary.database

import io.github.taetae98coding.diary.core.model.tag.TagDto
import kotlinx.coroutines.flow.Flow

public interface MemoTagDao {
	public suspend fun upsert(memoId: String, tagId: String)

	public suspend fun delete(memoId: String, tagId: String)

	public fun findTagByMemoId(memoId: String): Flow<List<TagDto>>
}
