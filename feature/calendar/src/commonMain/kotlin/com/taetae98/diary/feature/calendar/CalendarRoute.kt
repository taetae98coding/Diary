package com.taetae98.diary.feature.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.calendar.CalendarState
import com.taetae98.diary.library.compose.calendar.runtime.rememberCalendarState
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle
import kotlinx.datetime.Month

@Composable
internal fun CalendarRoute(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
) {
    val state = rememberCalendarState()

    CalendarScreen(
        modifier = modifier,
        state = state,
        schedule = viewModel.schedule.collectAsStateOnLifecycle(),
        holiday = viewModel.holiday.collectAsState()
    )

    ObserveCalendarState(
        state = state,
        onYearAndMonthChanged = viewModel::setYearAndMonth,
    )
}

@Composable
private fun ObserveCalendarState(
    state: CalendarState,
    onYearAndMonthChanged: (year: Int, month: Month) -> Unit
) {
    LaunchedEffect(state.currentYear, state.currentMonth) {
        onYearAndMonthChanged(state.currentYear, state.currentMonth)
    }
}