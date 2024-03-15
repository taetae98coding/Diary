package com.taetae98.diary.feature.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.calendar.CalendarState
import com.taetae98.diary.library.compose.calendar.model.DateRange
import com.taetae98.diary.library.compose.calendar.runtime.rememberCalendarState
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle
import com.taetae98.diary.library.kotlin.ext.toEpochMilliseconds
import kotlinx.datetime.Month

@Composable
@NonRestartableComposable
internal fun CalendarRoute(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
    navigateToMemoAdd: (dateRange: ClosedRange<Long>) -> Unit,
    navigateToMemoDetail: (memoId: String) -> Unit,
) {
    val state = rememberCalendarState()

    CalendarScreen(
        modifier = modifier,
        state = state,
        schedule = viewModel.schedule.collectAsStateOnLifecycle(),
        holiday = viewModel.holiday.collectAsState(),
        onItem = {
            when (it) {
                is MemoCalendarItemKey -> navigateToMemoDetail(it.key)
            }
        },
    )

    ObserveCalendarState(
        state = state,
        onYearAndMonthChanged = viewModel::setYearAndMonth,
        onDateSelectFinished = navigateToMemoAdd
    )
}

@Composable
private fun ObserveCalendarState(
    state: CalendarState,
    onYearAndMonthChanged: (year: Int, month: Month) -> Unit,
    onDateSelectFinished: (dateRange: ClosedRange<Long>) -> Unit,
) {
    val prevSelectDateRange: MutableState<DateRange?> = remember { mutableStateOf(null) }

    LaunchedEffect(state.currentYear, state.currentMonth) {
        onYearAndMonthChanged(state.currentYear, state.currentMonth)
    }

    LaunchedEffect(state.selectDateRange) {
        val captureDateRange = prevSelectDateRange.value
        if (captureDateRange != null && state.selectDateRange == null) {
            onDateSelectFinished(captureDateRange.start.toEpochMilliseconds()..captureDateRange.endInclusive.toEpochMilliseconds())
        }

        prevSelectDateRange.value = state.selectDateRange
    }
}