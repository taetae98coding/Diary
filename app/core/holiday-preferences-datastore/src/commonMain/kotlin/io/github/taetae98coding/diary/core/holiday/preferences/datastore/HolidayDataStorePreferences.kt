package io.github.taetae98coding.diary.core.holiday.preferences.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.github.taetae98coding.diary.core.holiday.preferences.HolidayPreferences
import io.github.taetae98coding.diary.library.datetime.todayIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.number

internal class HolidayDataStorePreferences(
    private val clock: Clock,
    private val dataStore: DataStore<Preferences>,
) : HolidayPreferences {
    private val memoryDirtyStore = mutableMapOf<Pair<Int, Month>, MutableStateFlow<Boolean>>()

    override fun isDirty(year: Int, month: Month): Flow<Boolean> {
        val dateStoreFlow = dataStore.data.map { it[dirtyKey(year, month)] }
            .map { it ?: false }
        val memoryFlow = memoryDirtyStore.getOrPut(year to month) { MutableStateFlow(false) }

        return combine(dateStoreFlow, memoryFlow) { dataStore, memory -> dataStore || memory }
    }

    override suspend fun setDirty(year: Int, month: Month) {
        if (isFuture(year, month)) {
            memoryDirtyStore.getOrPut(year to month) { MutableStateFlow(false) }.emit(true)
        } else {
            dataStore.edit { it[dirtyKey(year, month)] = true }
        }
    }

    private fun dirtyKey(year: Int, month: Month): Preferences.Key<Boolean> {
        return booleanPreferencesKey("$year${month.number.toString().padStart(2, '0')}")
    }

    private fun isFuture(year: Int, month: Month): Boolean {
        val today = LocalDate.todayIn(clock = clock)
        val target = LocalDate(year, month, 1)

        return today.monthsUntil(target) >= 0 && !(today.year == target.year && today.month == target.month)
    }
}
