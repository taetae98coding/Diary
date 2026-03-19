package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.CalendarMemoLocalEntity
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountCalendarMemoLocalDataSource {
    public fun get(
        accountId: Uuid,
        year: Int,
    ): Flow<List<CalendarMemoLocalEntity>>
}
