package io.github.taetae98coding.diary.core.calendar.compose.day

import io.github.taetae98coding.diary.library.datetime.invoke
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

internal class CalendarDayOfMonthState(val year: Int, val month: Month, val weekOfMonth: Int, val dayOfWeek: DayOfWeek) {
	val localDate = LocalDate(year, month, weekOfMonth, dayOfWeek)
	val isInMonth = year == localDate.year && month == localDate.month
}
