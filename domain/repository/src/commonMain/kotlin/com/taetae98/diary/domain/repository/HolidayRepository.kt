package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.account.holiday.Holiday
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Month

public interface HolidayRepository {
    public fun getHoliday(year: Int, month: Month): Flow<List<Holiday>>
}
