package io.github.taetae98coding.diary.domain.tag.repository

import io.github.taetae98coding.diary.core.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

public interface TagRepository {
	public suspend fun upsert(owner: String, list: List<Tag>)

	public fun findByIds(ids: Set<String>): Flow<List<Tag>>

	public fun findByUpdateAt(uid: String, updateAt: Instant): Flow<List<Tag>>
}
