package io.github.taetae98coding.diary.core.holiday.network.api

import io.github.taetae98coding.diary.core.holiday.network.api.entity.HolidayRemoteEntity

public interface HolidayRemoteDataSource {
    public suspend fun get(year: Int): List<HolidayRemoteEntity>
}
