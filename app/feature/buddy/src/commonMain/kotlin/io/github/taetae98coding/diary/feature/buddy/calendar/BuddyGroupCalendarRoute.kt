package io.github.taetae98coding.diary.feature.buddy.calendar

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.calendar.CalendarScaffold
import io.github.taetae98coding.diary.core.compose.calendar.CalendarScaffoldState
import io.github.taetae98coding.diary.core.compose.calendar.rememberCalendarScaffoldState
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun BuddyGroupCalendarRoute(
    navigateUp: () -> Unit,
    navigateToBuddyGroupMemoAdd: (ClosedRange<LocalDate>) -> Unit,
    navigateToBuddyGroupMemoDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    memoViewModel: BuddyGroupCalendarMemoViewModel = koinViewModel(),
    holidayViewModel: BuddyGroupCalendarHolidayViewModel = koinViewModel(),
) {
    val state = rememberCalendarScaffoldState(
        onFilter = {},
    )
    val memoList by memoViewModel.memoList.collectAsStateWithLifecycle()
    val holidayList by holidayViewModel.holidayList.collectAsStateWithLifecycle()

    CalendarScaffold(
        state = state,
        onSelectDate = navigateToBuddyGroupMemoAdd,
        hasFilterProvider = { false },
        textItemListProvider = { memoList },
        holidayListProvider = { holidayList },
        onCalendarItemClick = {
            when (it) {
                is MemoKey -> navigateToBuddyGroupMemoDetail(it.id)
            }
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                NavigateUpIcon()
            }
        },
    )

    Fetch(
        state = state,
        memoViewModel = memoViewModel,
        holidayViewModel = holidayViewModel,
    )

    LifecycleStartEffect(memoViewModel) {
        memoViewModel.refresh()
        onStopOrDispose {

        }
    }
}

@Composable
private fun Fetch(
    state: CalendarScaffoldState,
    memoViewModel: BuddyGroupCalendarMemoViewModel,
    holidayViewModel: BuddyGroupCalendarHolidayViewModel,
) {
    LaunchedEffect(state.calendarState.year, state.calendarState.month) {
        memoViewModel.fetchMemo(state.calendarState.year, state.calendarState.month)
        holidayViewModel.fetchHoliday(state.calendarState.year, state.calendarState.month)
    }
}
