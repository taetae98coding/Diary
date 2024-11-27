package io.github.taetae98coding.diary.data.calendar.repository

import io.github.taetae98coding.diary.core.filter.database.CalendarFilterDao
import io.github.taetae98coding.diary.domain.calendar.repository.CalendarRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class CalendarRepositoryImpl(
	private val localDataSource: CalendarFilterDao,
) : CalendarRepository {
	override suspend fun upsert(uid: String?, tagId: String) {
		localDataSource.upsert(uid, tagId)
	}

	override suspend fun delete(uid: String?, tagId: String) {
		localDataSource.delete(uid, tagId)
	}

	override fun findFilter(uid: String?): Flow<Set<String>> = localDataSource.findAll(uid)
}
