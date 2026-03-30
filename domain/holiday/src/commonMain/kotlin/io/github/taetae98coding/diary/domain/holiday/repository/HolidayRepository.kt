package io.github.taetae98coding.diary.domain.holiday.repository

import io.github.taetae98coding.diary.core.model.holiday.Holiday
import kotlinx.coroutines.flow.Flow

public interface HolidayRepository {
    public fun get(year: Int): Flow<List<Holiday>>

    public suspend fun fetch(year: Int)
}
