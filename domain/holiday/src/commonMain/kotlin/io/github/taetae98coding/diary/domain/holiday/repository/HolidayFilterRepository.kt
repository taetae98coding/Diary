package io.github.taetae98coding.diary.domain.holiday.repository

import kotlinx.coroutines.flow.Flow

public interface HolidayFilterRepository {
    public fun get(): Flow<List<String>>
}
