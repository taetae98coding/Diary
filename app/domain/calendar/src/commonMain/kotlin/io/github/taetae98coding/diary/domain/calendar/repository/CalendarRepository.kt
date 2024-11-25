package io.github.taetae98coding.diary.domain.calendar.repository

import kotlinx.coroutines.flow.Flow

public interface CalendarRepository {
	public suspend fun upsert(uid: String?, tagId: String)

	public suspend fun delete(uid: String?, tagId: String)

	public fun findFilter(uid: String?): Flow<Set<String>>
}
