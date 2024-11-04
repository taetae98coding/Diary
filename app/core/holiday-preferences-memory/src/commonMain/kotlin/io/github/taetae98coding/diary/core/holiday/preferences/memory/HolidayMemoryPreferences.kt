package io.github.taetae98coding.diary.core.holiday.preferences.memory

import io.github.taetae98coding.diary.core.holiday.preferences.HolidayPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Month

internal data object HolidayMemoryPreferences : HolidayPreferences{
    private val map = mutableMapOf<Pair<Int, Month>, MutableStateFlow<Boolean>>()

    override fun isDirty(year: Int, month: Month): Flow<Boolean> {
        return getFlow(year, month).asStateFlow()
    }

    override suspend fun setDirty(year: Int, month: Month) {
        getFlow(year, month).emit(true)
    }

    private fun getFlow(year: Int, month: Month): MutableStateFlow<Boolean> {
        return map.getOrPut(year to month) { MutableStateFlow(false) }
    }
}
