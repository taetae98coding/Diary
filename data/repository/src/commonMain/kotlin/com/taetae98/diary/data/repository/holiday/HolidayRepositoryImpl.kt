package com.taetae98.diary.data.repository.holiday

import com.taetae98.diary.data.dto.holiday.HolidayDto
import com.taetae98.diary.data.remote.api.HolidayRemoteDataSource
import com.taetae98.diary.domain.entity.holiday.Holiday
import com.taetae98.diary.domain.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Month
import org.koin.core.annotation.Factory

@Factory
internal class HolidayRepositoryImpl(
    private val remoteDataSource: HolidayRemoteDataSource
) : HolidayRepository {
    override fun getHoliday(year: Int, month: Month): Flow<List<Holiday>> {
        return flow {
            emit(remoteDataSource.getHoliday(year, month))
        }.map {
            it.map(HolidayDto::toDomain)
        }
    }
}
