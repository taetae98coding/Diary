package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.datasource.CalendarMemoFilterTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.CalendarMemoFilterTagLocalEntity
import io.github.taetae98coding.diary.domain.memo.repository.CalendarMemoFilterTagRepository
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
public class CalendarMemoFilterTagRepositoryImpl(private val calendarMemoFilterTagLocalDataSource: CalendarMemoFilterTagLocalDataSource) : CalendarMemoFilterTagRepository {
    override suspend fun upsert(tagId: Uuid) {
        calendarMemoFilterTagLocalDataSource.upsert(CalendarMemoFilterTagLocalEntity(tagId))
    }

    override suspend fun delete(tagId: Uuid) {
        calendarMemoFilterTagLocalDataSource.delete(CalendarMemoFilterTagLocalEntity(tagId))
    }
}
