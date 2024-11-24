package io.github.taetae98coding.diary.core.filter.database

import kotlinx.coroutines.flow.Flow

public interface CalendarFilterDao {
	public suspend fun upsert(uid: String?, tagId: String)

	public suspend fun delete(uid: String?, tagId: String)

	public fun findAll(uid: String?): Flow<Set<String>>
}
