package io.github.taetae98coding.diary.core.holiday.database.room

import io.github.taetae98coding.diary.core.holiday.database.HolidayDao
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Month
import kotlinx.datetime.number
import org.koin.core.annotation.Factory

@Factory
internal class HolidayRoomDao(
	private val database: HolidayDatabase,
) : HolidayDao {
	override fun findHoliday(year: Int, month: Month): Flow<List<Holiday>> =
		database
			.holidayDao()
			.findHoliday(year, month.number)
			.mapCollectionLatest(HolidayEntity::toHoliday)

	override suspend fun upsert(year: Int, month: Month, holidayList: List<Holiday>) {
		database.holidayDao().upsert(year, month.number, holidayList.map(Holiday::toEntity))
	}
}
