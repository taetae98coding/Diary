package io.github.taetae98coding.diary.core.compose.calendar

import androidx.compose.ui.focus.FocusRequester
import io.github.taetae98coding.diary.core.calendar.compose.state.CalendarState

public class CalendarScaffoldState(
    public val onFilter: () -> Unit,
    public val calendarState: CalendarState,
) {
    public val focusRequester: FocusRequester = FocusRequester()
}
