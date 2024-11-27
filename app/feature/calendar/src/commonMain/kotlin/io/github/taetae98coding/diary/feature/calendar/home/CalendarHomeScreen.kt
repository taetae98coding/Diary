package io.github.taetae98coding.diary.feature.calendar.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import io.github.taetae98coding.diary.core.calendar.compose.Calendar
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.core.calendar.compose.modifier.calendarDateRangeSelectable
import io.github.taetae98coding.diary.core.calendar.compose.topbar.CalendarTopBar
import io.github.taetae98coding.diary.core.calendar.compose.topbar.TodayIcon
import io.github.taetae98coding.diary.core.design.system.icon.FilterIcon
import io.github.taetae98coding.diary.core.design.system.icon.FilterOffIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.library.datetime.todayIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
internal fun CalendarHomeScreen(
	state: CalendarHomeScreenState,
	onSelectDate: (ClosedRange<LocalDate>) -> Unit,
	onFilter: () -> Unit,
	hasFilterProvider: () -> Boolean,
	textItemListProvider: () -> List<CalendarItemUiState.Text>,
	holidayListProvider: () -> List<CalendarItemUiState.Holiday>,
	onCalendarItemClick: (Any) -> Unit,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			CalendarTopBar(
				state = state.calendarState,
				actions = {
					val coroutineScope = rememberCoroutineScope()

					IconButton(onClick = onFilter) {
						Crossfade(hasFilterProvider()) { hasFilter ->
							if (hasFilter) {
								FilterIcon(tint = DiaryTheme.color.primary)
							} else {
								FilterOffIcon()
							}
						}
					}

					IconButton(onClick = { coroutineScope.launch { state.calendarState.animateScrollTo(LocalDate.todayIn()) } }) {
						TodayIcon()
					}
				},
			)
		},
	) {
		var today by remember { mutableStateOf(LocalDate.todayIn()) }

		Calendar(
			state = state.calendarState,
			primaryDateListProvider = { listOf(today) },
			textItemListProvider = textItemListProvider,
			holidayListProvider = holidayListProvider,
			onCalendarItemClick = onCalendarItemClick,
			modifier = Modifier
				.fillMaxSize()
				.padding(it)
				.calendarDateRangeSelectable(
					state = state.calendarState,
					onSelectDate = onSelectDate,
				),
		)

		LifecycleResumeEffect(Unit) {
			today = LocalDate.todayIn()
			onPauseOrDispose { }
		}
	}
}
