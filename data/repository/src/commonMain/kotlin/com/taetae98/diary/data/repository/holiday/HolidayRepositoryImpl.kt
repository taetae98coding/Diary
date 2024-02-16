package com.taetae98.diary.data.repository.holiday

import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.holiday.HolidayDto
import com.taetae98.diary.data.local.api.HolidayLocalDataSource
import com.taetae98.diary.data.remote.api.HolidayRemoteDataSource
import com.taetae98.diary.domain.entity.holiday.Holiday
import com.taetae98.diary.domain.repository.HolidayRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class HolidayRepositoryImpl(
    private val localDataSource: HolidayLocalDataSource,
    private val remoteDataSource: HolidayRemoteDataSource,
    @Named(CoroutinesModule.PROCESS)
    private val coroutineScope: CoroutineScope
) : HolidayRepository {
    override fun getHoliday(year: Int, month: Month): Flow<List<Holiday>> {
        coroutineScope.launch {
            runCatching { updateHolidayList(year, month) }
                .onFailure { it.printStackTrace() }
        }

        return localDataSource.getHoliday(year, month)
            .map { it.map(HolidayDto::toDomain) }
    }

    private suspend fun updateHolidayList(year: Int, month: Month) {
        localDataSource.setHolidayList(year, month, remoteDataSource.getHoliday(year, month))
    }
}
