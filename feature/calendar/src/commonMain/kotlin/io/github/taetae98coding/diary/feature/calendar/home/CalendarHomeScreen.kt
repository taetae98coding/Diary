package io.github.taetae98coding.diary.feature.calendar.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.compose.core.dialog.LocalDatePickerDialogHost
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import kotlin.time.Clock
import kotlin.uuid.Uuid
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CalendarHomeScreen(
    navigateToMemoAdd: (LocalDateRange) -> Unit,
    navigateToMemoDetail: (Uuid) -> Unit,
    navigateToFilter: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: CalendarHomeViewModel = koinViewModel(),
    memoViewModel: CalendarMemoViewModel = koinViewModel(),
    routineViewModel: CalendarRoutineViewModel = koinViewModel(),
    holidayViewModel: CalendarHolidayViewModel = koinViewModel(),
    weatherViewModel: CalendarWeatherViewModel = koinViewModel(),
) {
    val isSyncing by homeViewModel.isSyncing.collectAsStateWithLifecycle()
    val memoList by memoViewModel.calendarMemo.collectAsStateWithLifecycle()
    val routineList by routineViewModel.calendarRoutine.collectAsStateWithLifecycle()
    val hasFilter by memoViewModel.hasFilter.collectAsStateWithLifecycle()
    val holidayList by holidayViewModel.holiday.collectAsStateWithLifecycle()
    val weatherList by weatherViewModel.weather.collectAsStateWithLifecycle()
    val state = rememberCalendarScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    SyncFailedEffect(
        viewModel = homeViewModel,
        state = state,
    )

    CalendarScaffold(
        modifier = modifier,
        state = state,
        isFetchingProvider = { isSyncing },
        hasFilterProvider = { hasFilter },
        memoListProvider = { memoList },
        routineListProvider = { routineList },
        holidayListProvider = { holidayList },
        weatherListProvider = { weatherList },
        onFetch = homeViewModel::sync,
        onFilterClick = navigateToFilter,
        onLocalDateRangeSelect = navigateToMemoAdd,
        onMemoClick = navigateToMemoDetail,
    )

    LocalDatePickerDialogHost(
        state = state.localDatePickerDialogState,
        localDateProvider = { state.calendarState.yearMonth.firstDay },
        onSelect = { coroutineScope.launch { state.calendarState.animateScrollToYearMonth(it.yearMonth) } },
    )

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        state.updatePrimaryDate(Clock.System.todayIn(TimeZone.currentSystemDefault()))
        weatherViewModel.fetch()
    }

    LifecycleStartEffect(state.calendarState.yearMonth) {
        memoViewModel.fetchYearMonth(state.calendarState.yearMonth)
        routineViewModel.fetchYearMonth(state.calendarState.yearMonth)
        holidayViewModel.fetch(state.calendarState.yearMonth)
        onStopOrDispose { }
    }
}

@Composable
private fun SyncFailedEffect(
    viewModel: CalendarHomeViewModel,
    state: CalendarScaffoldState,
) {
    val isFailed by viewModel.isFailed.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isFailed) {
        if (isFailed) {
            coroutineScope.launch { state.hostState.showImmediate("오프라인입니다.") }
        }
    }
}
