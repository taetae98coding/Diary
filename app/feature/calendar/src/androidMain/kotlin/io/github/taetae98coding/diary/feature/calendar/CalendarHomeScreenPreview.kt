package io.github.taetae98coding.diary.feature.calendar

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.calendar.home.CalendarHomeScreen
import io.github.taetae98coding.diary.feature.calendar.home.rememberCalendarHomeScreenState

@DiaryPreview
@Composable
private fun CalendarHomeScreenPreview() {
	DiaryTheme {
		CalendarHomeScreen(
			state = rememberCalendarHomeScreenState(),
			onSelectDate = {},
			onFilter = {},
			hasFilterProvider = { false },
			textItemListProvider = { emptyList() },
			holidayListProvider = { emptyList() },
			onCalendarItemClick = {},
		)
	}
}
