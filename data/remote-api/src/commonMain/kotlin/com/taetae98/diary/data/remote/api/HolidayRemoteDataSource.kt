package com.taetae98.diary.data.remote.api

import com.taetae98.diary.data.dto.holiday.HolidayDto
import kotlinx.datetime.Month

public interface HolidayRemoteDataSource {
    public suspend fun getHoliday(year: Int, month: Month): List<HolidayDto>
}
