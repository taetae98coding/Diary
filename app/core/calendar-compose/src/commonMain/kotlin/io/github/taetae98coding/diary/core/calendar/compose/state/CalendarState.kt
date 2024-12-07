package io.github.taetae98coding.diary.core.calendar.compose.state

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.taetae98coding.diary.library.datetime.todayIn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus

public class CalendarState internal constructor(
	internal val pagerState: PagerState,
) {
	internal val localDate: LocalDate
		get() = LocalDate(1, 1, 1).plus(pagerState.currentPage, DateTimeUnit.MONTH)

	internal var selectedDateRange: ClosedRange<LocalDate>? by mutableStateOf(null)
		private set

	public val year: Int
		get() = localDate.year

	public val month: Month
		get() = localDate.month

	public val isScrollInProgress: Boolean
		get() = pagerState.isScrollInProgress

	public suspend fun animateScrollToToday() {
		animateScrollTo(LocalDate.todayIn())
	}

	public suspend fun animateScrollTo(localDate: LocalDate) {
		pagerState.animateScrollToPage(page(localDate))
	}

	public suspend fun animateScrollToBackward() {
		if (pagerState.canScrollBackward) {
			pagerState.animateScrollToPage(pagerState.currentPage - 1)
		}
	}

	public suspend fun animateScrollToForward() {
		if (pagerState.canScrollForward) {
			pagerState.animateScrollToPage(pagerState.currentPage + 1)
		}
	}

	internal fun drag(dateRange: ClosedRange<LocalDate>) {
		selectedDateRange = dateRange
	}

	internal fun finishDrag() {
		selectedDateRange = null
	}

	public companion object {
		public fun page(localDate: LocalDate): Int = (localDate.year - 1) * 12 + (localDate.monthNumber - 1)
	}
}
