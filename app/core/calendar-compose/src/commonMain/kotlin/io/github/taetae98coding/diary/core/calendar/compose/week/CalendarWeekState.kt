package io.github.taetae98coding.diary.core.calendar.compose.week

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.taetae98coding.diary.library.datetime.invoke
import io.github.taetae98coding.diary.library.datetime.isOverlap
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

internal class CalendarWeekState(val year: Int, val month: Month, val weekOfMonth: Int) {
	val dateRange = LocalDate(year, month, weekOfMonth, DayOfWeek.SUNDAY)..LocalDate(year, month, weekOfMonth, DayOfWeek.SATURDAY)

	var selectedDateRange: ClosedRange<LocalDate>? by mutableStateOf(null)
		private set

	fun drag(range: ClosedRange<LocalDate>) {
		if (!dateRange.isOverlap(range)) {
			selectedDateRange = null
			return
		}

		selectedDateRange = range.start.coerceIn(dateRange)..range.endInclusive.coerceIn(dateRange)
	}

	fun finishDrag() {
		selectedDateRange = null
	}
}
