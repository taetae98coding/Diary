package io.github.taetae98coding.diary.core.diary.database

import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.model.tag.TagDto
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

public interface TagDao {
	public suspend fun upsert(tag: TagDto)

	public suspend fun upsert(tagList: List<TagDto>)

	public suspend fun update(tagId: String, detail: TagDetail)

	public suspend fun updateFinish(tagId: String, isFinish: Boolean)

	public suspend fun updateDelete(tagId: String, isDelete: Boolean)

	public fun find(tagId: String, filterNotDelete: Boolean): Flow<TagDto?>

	public fun findByIds(tagIds: Set<String>, filterNotDelete: Boolean): Flow<List<TagDto>>

	public fun page(owner: String?): Flow<List<TagDto>>

	public fun getLastServerUpdateAt(owner: String?): Flow<Instant?>
}
