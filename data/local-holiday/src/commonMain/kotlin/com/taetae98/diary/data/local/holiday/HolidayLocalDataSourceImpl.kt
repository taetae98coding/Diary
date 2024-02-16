package com.taetae98.diary.data.local.holiday

import com.taetae98.diary.data.dto.holiday.HolidayDto
import com.taetae98.diary.data.local.api.HolidayLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.koin.core.annotation.Factory

@Factory
internal class HolidayLocalDataSourceImpl : HolidayLocalDataSource {
    override fun getHoliday(year: Int, month: Month): Flow<List<HolidayDto>> {
        val date = LocalDate(year, month, 1)
        return flowOf(listOf(HolidayDto("$year / $month", date, date)))
    }

    override suspend fun setHolidayList(year: Int, month: Month, list: List<HolidayDto>) {

    }
}