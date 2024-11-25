package io.github.taetae98coding.diary.core.calendar.compose.month

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

internal class CalendarMonthState(val year: Int, val month: Month) {
	var selectedDateRange: ClosedRange<LocalDate>? by mutableStateOf(null)
		private set

	fun drag(dateRange: ClosedRange<LocalDate>) {
		selectedDateRange = dateRange
	}

	fun finishDrag() {
		selectedDateRange = null
	}
}
