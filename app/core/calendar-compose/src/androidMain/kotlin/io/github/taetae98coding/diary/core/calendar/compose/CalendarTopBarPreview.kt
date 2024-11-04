package io.github.taetae98coding.diary.core.calendar.compose

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.calendar.compose.state.rememberCalendarState
import io.github.taetae98coding.diary.core.calendar.compose.topbar.CalendarTopBar
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@DiaryPreview
@Composable
private fun CalendarTopBarPreview() {
    DiaryTheme {
        CalendarTopBar(state = rememberCalendarState())
    }
}
