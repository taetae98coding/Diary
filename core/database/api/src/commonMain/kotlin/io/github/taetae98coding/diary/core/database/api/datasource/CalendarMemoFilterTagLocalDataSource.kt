package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.CalendarMemoFilterTagLocalEntity

public interface CalendarMemoFilterTagLocalDataSource {
    public suspend fun upsert(entity: CalendarMemoFilterTagLocalEntity)
    public suspend fun delete(entity: CalendarMemoFilterTagLocalEntity)
}
