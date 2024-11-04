package io.github.taetae98coding.diary.core.holiday.database

import io.github.taetae98coding.diary.core.model.holiday.Holiday
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Month

public interface HolidayDao {
    public fun findHoliday(year: Int, month: Month): Flow<List<Holiday>>

    public suspend fun upsert(year: Int, month: Month, holidayList: List<Holiday>)
}
