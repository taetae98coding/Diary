package com.taetae98.diary.data.local.holiday

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.holiday.HolidayDto
import com.taetae98.diary.data.local.api.HolidayLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class HolidayLocalDataSourceImpl(
    private val database: HolidayDatabase,
    @Named(CoroutinesModule.DATABASE)
    private val dispatcher: CoroutineDispatcher,
) : HolidayLocalDataSource {
    override fun getHoliday(year: Int, month: Month): Flow<List<HolidayDto>> {
        val startAt = LocalDate(year, month, 1)
        val endAt = startAt.plus(1, DateTimeUnit.MONTH)
            .minus(1, DateTimeUnit.DAY)

        return database.holidayEntityQueries.findByDate(startAt, endAt, ::mapToHolidayDto)
            .asFlow()
            .mapToList(dispatcher)
    }

    override suspend fun setHolidayList(year: Int, month: Month, list: List<HolidayDto>) {
        val startAt = LocalDate(year, month, 1)
        val endAt = startAt.plus(1, DateTimeUnit.MONTH)
            .minus(1, DateTimeUnit.DAY)

        database.transaction {
            database.holidayEntityQueries.deleteByDate(startAt, endAt)
            list.map(HolidayDto::toEntity).forEach { database.holidayEntityQueries.insert(it) }
        }
    }
}