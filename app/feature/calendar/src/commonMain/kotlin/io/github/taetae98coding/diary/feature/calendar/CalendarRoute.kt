package io.github.taetae98coding.diary.feature.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CalendarRoute(
    navigateToMemoAdd: (ClosedRange<LocalDate>) -> Unit,
    navigateToMemoDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    memoViewModel: CalendarMemoViewModel = koinViewModel(),
    holidayViewModel: HolidayViewModel = koinViewModel(),
) {
    val state = rememberCalendarScreenState()
    val textItemList by memoViewModel.textItemList.collectAsStateWithLifecycle()
    val holidayList by holidayViewModel.holidayList.collectAsStateWithLifecycle()

    CalendarScreen(
        state = state,
        onSelectDate = navigateToMemoAdd,
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
        memoViewModel = memoViewModel,
        holidayViewModel = holidayViewModel,
    )
}

@Composable
private fun Fetch(
    state: CalendarScreenState,
    memoViewModel: CalendarMemoViewModel,
    holidayViewModel: HolidayViewModel,
) {
    LaunchedEffect(state.calendarState.year, state.calendarState.month) {
        memoViewModel.fetchMemo(state.calendarState.year, state.calendarState.month)
        holidayViewModel.fetchHoliday(state.calendarState.year, state.calendarState.month)
    }
}
