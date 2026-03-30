package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.CalendarMemoFilterTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.CalendarMemoFilterTagLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import org.koin.core.annotation.Factory

@Factory
internal class CalendarMemoFilterTagLocalDataSourceImpl(private val database: DiaryDatabase) : CalendarMemoFilterTagLocalDataSource {
    override suspend fun upsert(entity: CalendarMemoFilterTagLocalEntity) {
        database.calendarMemoFilterTagDao().upsert(entity)
    }

    override suspend fun delete(entity: CalendarMemoFilterTagLocalEntity) {
        database.calendarMemoFilterTagDao().delete(entity)
    }
}
