package io.github.taetae98coding.diary.core.filter.database.room.dao

import io.github.taetae98coding.diary.core.filter.database.CalendarFilterDao
import io.github.taetae98coding.diary.core.filter.database.room.FilterDatabase
import io.github.taetae98coding.diary.core.filter.database.room.entity.CalendarFilterEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class CalendarFilterRoomDao(
	private val database: FilterDatabase,
) : CalendarFilterDao {
	override suspend fun upsert(uid: String?, tagId: String) {
		database.calendar().upsert(CalendarFilterEntity(uid.orEmpty(), tagId))
	}

	override suspend fun delete(uid: String?, tagId: String) {
		database.calendar().delete(CalendarFilterEntity(uid.orEmpty(), tagId))
	}

	override fun findAll(uid: String?): Flow<Set<String>> =
		database
			.calendar()
			.findAll(uid.orEmpty())
			.mapLatest(List<String>::toSet)
}
