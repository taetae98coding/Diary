package com.taetae98.diary.data.local.api

import com.taetae98.diary.data.dto.holiday.HolidayDto
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Month

public interface HolidayLocalDataSource {
    public fun getHoliday(year: Int, month: Month): Flow<List<HolidayDto>>

    public suspend fun setHolidayList(year: Int, month: Month, list: List<HolidayDto>)
}