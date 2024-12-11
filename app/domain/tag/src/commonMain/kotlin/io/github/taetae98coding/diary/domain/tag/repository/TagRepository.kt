package io.github.taetae98coding.diary.domain.tag.repository

import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import kotlinx.coroutines.flow.Flow

public interface TagRepository {
	public suspend fun upsert(owner: String, tag: Tag)

	public suspend fun update(tagId: String, detail: TagDetail)

	public suspend fun updateDelete(tagId: String, isDelete: Boolean)

	public suspend fun updateFinish(tagId: String, isFinish: Boolean)

	public fun page(owner: String?): Flow<List<Tag>>

	public fun getById(tagId: String): Flow<Tag?>

	public fun getByIds(tagIds: Set<String>): Flow<List<Tag>>

	public suspend fun getNextTagId(): String
}
