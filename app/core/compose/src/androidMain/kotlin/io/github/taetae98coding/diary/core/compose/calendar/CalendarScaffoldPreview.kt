package io.github.taetae98coding.diary.core.compose.calendar

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@DiaryPreview
@Composable
private fun CalendarHomeScreenPreview() {
	DiaryTheme {
		CalendarScaffold(
			state = rememberCalendarScaffoldState(
				onFilter = {},
			),
			onSelectDate = {},
			hasFilterProvider = { false },
			textItemListProvider = { emptyList() },
			holidayListProvider = { emptyList() },
			onCalendarItemClick = {},
		)
	}
}
