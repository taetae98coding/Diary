package io.github.taetae98coding.diary.data.holiday.repository

import io.github.taetae98coding.diary.core.holiday.database.api.HolidayLocalDataSource
import io.github.taetae98coding.diary.core.holiday.network.api.HolidayRemoteDataSource
import io.github.taetae98coding.diary.core.holiday.network.api.entity.HolidayRemoteEntity
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.domain.holiday.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
public class HolidayRepositoryImpl(
    private val holidayRemoteDataSource: HolidayRemoteDataSource,
    private val holidayLocalDataSource: HolidayLocalDataSource,
) : HolidayRepository {
    private val fetchedYears = mutableSetOf<Int>()

    override fun get(year: Int): Flow<List<Holiday>> {
        return holidayLocalDataSource.get(year)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun fetch(year: Int) {
        if (year in fetchedYears) return

        val remoteHolidays = holidayRemoteDataSource.get(year)
        holidayLocalDataSource.upsert(year, remoteHolidays.map(HolidayRemoteEntity::toLocal))
        fetchedYears.add(year)
    }
}
