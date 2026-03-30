package io.github.taetae98coding.diary.core.holiday.database.api

import io.github.taetae98coding.diary.core.holiday.database.api.entity.HolidayLocalEntity
import kotlinx.coroutines.flow.Flow

public interface HolidayLocalDataSource {
    public fun get(year: Int): Flow<List<HolidayLocalEntity>>

    public suspend fun upsert(
        year: Int,
        entities: List<HolidayLocalEntity>,
    )
}
