package com.taetae98.diary.data.remote.impl.holiday

import com.taetae98.diary.data.dto.holiday.HolidayDto
import com.taetae98.diary.data.remote.api.HolidayRemoteDataSource
import kotlinx.datetime.Month
import org.koin.core.annotation.Factory

@Factory
internal class HolidayRemoteDataSourceImpl : HolidayRemoteDataSource {
    override suspend fun getHoliday(year: Int, month: Month): List<HolidayDto> {
        return emptyList()
    }
}