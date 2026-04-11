package io.github.taetae98coding.diary.feature.calendar.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import io.github.taetae98coding.diary.compose.calendar.CalendarState
import io.github.taetae98coding.diary.compose.calendar.rememberCalendarState
import io.github.taetae98coding.diary.compose.core.dialog.DialogState
import io.github.taetae98coding.diary.compose.core.dialog.rememberDialogState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

@Stable
internal class CalendarScaffoldState(
    internal val calendarState: CalendarState,
    internal val localDatePickerDialogState: DialogState,
    initialPrimaryDate: LocalDate? = null,
) {
    internal var primaryDate: LocalDate? by mutableStateOf(initialPrimaryDate)
        private set

    val hostState: SnackbarHostState = SnackbarHostState()

    internal fun updatePrimaryDate(localDate: LocalDate?) {
        primaryDate = localDate
    }
}

@Composable
internal fun rememberCalendarScaffoldState(
    initialYearMonth: YearMonth? = null,
    initialPrimaryDate: LocalDate? = null,
): CalendarScaffoldState {
    val calendarState = if (initialYearMonth != null) {
        rememberCalendarState(initialYearMonth = initialYearMonth)
    } else {
        rememberCalendarState()
    }
    val datePickerDialogState = rememberDialogState()

    return retain(
        calendarState,
        datePickerDialogState,
    ) {
        CalendarScaffoldState(
            calendarState = calendarState,
            localDatePickerDialogState = datePickerDialogState,
            initialPrimaryDate = initialPrimaryDate,
        )
    }
}
