package io.github.taetae98coding.diary.core.calendar.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.core.calendar.compose.state.rememberCalendarState
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import kotlinx.datetime.LocalDate

@DiaryPreview
@Composable
private fun CalendarPreview() {
	DiaryTheme {
		val state = rememberCalendarState(
			initialLocalDate = LocalDate(2000, 1, 1),
		)

		Calendar(
			state = state,
			primaryDateListProvider = {
				listOf(
					LocalDate(2000, 1, 1),
					LocalDate(2000, 1, 31),
				)
			},
			textItemListProvider = {
				listOf(
					CalendarItemUiState.Text("2-5", "2-5", (0xFFFFFFFF).toInt(), LocalDate(2000, 1, 2), LocalDate(2000, 1, 5)),
				)
			},
			holidayListProvider = {
				listOf(
					CalendarItemUiState.Holiday("새해", LocalDate(2000, 1, 1), LocalDate(2000, 1, 1)),
				)
			},
			onCalendarItemClick = {},
		)

		LaunchedEffect(state) {
			state.drag(LocalDate(2000, 1, 7)..LocalDate(2000, 1, 17))
		}
	}
}
