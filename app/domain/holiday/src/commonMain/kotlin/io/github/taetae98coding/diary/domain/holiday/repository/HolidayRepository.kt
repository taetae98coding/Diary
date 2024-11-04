package io.github.taetae98coding.diary.domain.holiday.repository

import io.github.taetae98coding.diary.core.model.holiday.Holiday
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Month

public interface HolidayRepository {
    public fun findHoliday(year: Int, month: Month): Flow<List<Holiday>>
}
