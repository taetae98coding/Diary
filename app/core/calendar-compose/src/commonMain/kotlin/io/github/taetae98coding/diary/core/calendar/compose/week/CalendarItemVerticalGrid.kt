package io.github.taetae98coding.diary.core.calendar.compose.week

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.calendar.compose.CalendarDefaults
import io.github.taetae98coding.diary.core.calendar.compose.color.CalendarColors
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.library.datetime.christ
import io.github.taetae98coding.diary.library.datetime.isOverlap

@Composable
internal fun CalendarItemVerticalGrid(
	state: CalendarWeekState,
	textItemListProvider: () -> List<CalendarItemUiState.Text>,
	holidayListProvider: () -> List<CalendarItemUiState.Holiday>,
	onCalendarItemClick: (Any) -> Unit,
	modifier: Modifier = Modifier,
	colors: CalendarColors = CalendarDefaults.colors(),
) {
	val items by remember {
		derivedStateOf {
			val itemList = buildList {
				addAll(holidayListProvider())
				addAll(textItemListProvider())
			}.filter {
				it.isOverlap(state.dateRange)
			}.sorted().toMutableList()

			buildList {
				while (itemList.isNotEmpty()) {
					var dayOfWeek = 0

					buildList {
						while (true) {
							val item = itemList.find { dayOfWeek <= it.startChrist(state) } ?: break
							val start = item.startChrist(state)
							val endInclusive = item.endInclusiveChrist(state)

							val spaceWeight = start - dayOfWeek
							if (spaceWeight > 0) {
								add(WeekItem.Space(spaceWeight.toFloat()))
							}

							val weight = endInclusive - start + 1F
							val weekTextItem = when (item) {
								is CalendarItemUiState.Holiday -> {
									WeekItem.Holiday(
										key = item.key,
										name = item.text,
										weight = weight,
									)
								}

								is CalendarItemUiState.Text -> {
									WeekItem.Text(
										key = item.key,
										name = item.text,
										weight = weight,
										color = Color(item.color),
									)
								}
							}

							add(weekTextItem)
							itemList.remove(item)
							dayOfWeek = endInclusive + 1
						}

						if (dayOfWeek <= kotlinx.datetime.DayOfWeek.SATURDAY.christ) {
							val weight = kotlinx.datetime.DayOfWeek.SATURDAY.christ - dayOfWeek + 1F
							add(WeekItem.Space(weight))
						}
					}.also {
						add(it)
					}
				}
			}
		}
	}

	LazyColumn(
		modifier = modifier,
		contentPadding = PaddingValues(2.dp),
		verticalArrangement = Arrangement.spacedBy(2.dp),
	) {
		items(items = items) {
			WeekItemRow(
				items = it,
				onWeekItemClick = onCalendarItemClick,
				colors = colors,
			)
		}
	}
}

private fun CalendarItemUiState.startChrist(state: CalendarWeekState): Int = start.coerceAtLeast(state.dateRange.start).dayOfWeek.christ

private fun CalendarItemUiState.endInclusiveChrist(state: CalendarWeekState): Int = endInclusive.coerceAtMost(state.dateRange.endInclusive).dayOfWeek.christ
