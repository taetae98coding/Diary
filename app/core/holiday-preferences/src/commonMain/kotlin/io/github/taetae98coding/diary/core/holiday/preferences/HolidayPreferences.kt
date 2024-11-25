package io.github.taetae98coding.diary.core.holiday.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Month

public interface HolidayPreferences {
	public fun isDirty(year: Int, month: Month): Flow<Boolean>

	public suspend fun setDirty(year: Int, month: Month)
}
