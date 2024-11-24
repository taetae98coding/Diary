package io.github.taetae98coding.diary.core.calendar.compose.state

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.taetae98coding.diary.library.datetime.todayIn
import kotlinx.datetime.LocalDate

@Composable
public fun rememberCalendarState(
	initialLocalDate: LocalDate = LocalDate.todayIn(),
): CalendarState {
	val pagerState = rememberPagerState(CalendarState.page(initialLocalDate)) { Int.MAX_VALUE }

	return remember {
		CalendarState(
			pagerState = pagerState,
		)
	}
}
