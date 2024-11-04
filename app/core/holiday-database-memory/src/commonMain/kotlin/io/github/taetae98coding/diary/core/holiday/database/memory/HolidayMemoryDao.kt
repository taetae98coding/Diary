package io.github.taetae98coding.diary.core.holiday.database.memory

import io.github.taetae98coding.diary.core.holiday.database.HolidayDao
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Month

internal data object HolidayMemoryDao : HolidayDao {
    private val flow = mutableMapOf<Pair<Int, Month>, MutableStateFlow<List<Holiday>>>()

    override fun findHoliday(year: Int, month: Month): Flow<List<Holiday>> {
        return getFlow(year, month).asStateFlow()
    }

    override suspend fun upsert(year: Int, month: Month, holidayList: List<Holiday>) {
        getFlow(year, month).emit(holidayList)
    }

    private fun getFlow(year: Int, month: Month): MutableStateFlow<List<Holiday>> {
        return flow.getOrPut(year to month) { MutableStateFlow(emptyList()) }
    }
}
