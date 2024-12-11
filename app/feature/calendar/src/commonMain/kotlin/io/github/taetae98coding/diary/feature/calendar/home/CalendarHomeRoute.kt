package io.github.taetae98coding.diary.feature.calendar.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.calendar.CalendarScaffold
import io.github.taetae98coding.diary.core.compose.calendar.CalendarScaffoldState
import io.github.taetae98coding.diary.core.compose.calendar.rememberCalendarScaffoldState
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CalendarHomeRoute(
	navigateToCalendarFilter: () -> Unit,
	navigateToMemoAdd: (ClosedRange<LocalDate>) -> Unit,
	navigateToMemoDetail: (String) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: CalendarHomeViewModel = koinViewModel(),
	holidayViewModel: CalendarHomeHolidayViewModel = koinViewModel(),
) {
	val state = rememberCalendarScaffoldState(
		onFilter = navigateToCalendarFilter,
	)
	val hasFilter by viewModel.hasFilter.collectAsStateWithLifecycle()
	val textItemList by viewModel.textItemList.collectAsStateWithLifecycle()
	val holidayList by holidayViewModel.holidayList.collectAsStateWithLifecycle()

	CalendarScaffold(
		state = state,
		onSelectDate = navigateToMemoAdd,
		hasFilterProvider = { hasFilter },
		textItemListProvider = { textItemList },
		holidayListProvider = { holidayList },
		onCalendarItemClick = { key ->
			when (key) {
				is MemoKey -> navigateToMemoDetail(key.id)
			}
		},
		modifier = modifier,
	)

	Fetch(
		state = state,
		memoViewModel = viewModel,
		holidayViewModel = holidayViewModel,
	)
}

@Composable
private fun Fetch(
	state: CalendarScaffoldState,
	memoViewModel: CalendarHomeViewModel,
	holidayViewModel: CalendarHomeHolidayViewModel,
) {
	LaunchedEffect(state.calendarState.year, state.calendarState.month) {
		memoViewModel.fetchMemo(state.calendarState.year, state.calendarState.month)
		holidayViewModel.fetchHoliday(state.calendarState.year, state.calendarState.month)
	}
}
