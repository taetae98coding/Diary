package io.github.taetae98coding.diary.core.calendar.compose.modifier

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import io.github.taetae98coding.diary.core.calendar.compose.state.CalendarState
import io.github.taetae98coding.diary.library.datetime.invoke
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Composable
public fun Modifier.calendarDateRangeSelectable(
	state: CalendarState,
	onSelectDate: (ClosedRange<LocalDate>) -> Unit,
): Modifier {
	val haptic = LocalHapticFeedback.current
	val coroutineScope = rememberCoroutineScope()

	fun drag(dateRange: ClosedRange<LocalDate>) {
		state.drag(dateRange)
		haptic.performHapticFeedback(HapticFeedbackType.LongPress)
	}

	return pointerInput(state) {
		lateinit var dragStartDate: LocalDate
		lateinit var currentDragDate: LocalDate

		fun getSelectDate(): ClosedRange<LocalDate> = minOf(dragStartDate, currentDragDate)..maxOf(dragStartDate, currentDragDate)

		detectDragGesturesAfterLongPress(
			onDragStart = { offset ->
				val (row, column) = offsetToRowColumn(offset)
				val date = state.localDate(row, column)

				dragStartDate = date
				currentDragDate = date
				drag(getSelectDate())
			},
			onDragEnd = {
				state.finishDrag()
				onSelectDate(getSelectDate())
			},
			onDragCancel = state::finishDrag,
			onDrag = { change, _ ->
				val (row, column) = offsetToRowColumn(change.position)
				val date = state.localDate(row, column)

				if (!state.pagerState.isScrollInProgress) {
					if (column <= 0.33F) {
						coroutineScope.launch { state.animateScrollToBackward() }
					} else if (column >= 6.66F) {
						coroutineScope.launch { state.animateScrollToForward() }
					}
				}

				if (currentDragDate != date) {
					currentDragDate = date
					drag(getSelectDate())
				}
			},
		)
	}
}

private fun PointerInputScope.offsetToRowColumn(offset: Offset): Pair<Float, Float> = (offset.y * 6 / size.height) to (offset.x * 7 / size.width)

private fun CalendarState.localDate(row: Float, column: Float): LocalDate =
	LocalDate(year, month, 0, DayOfWeek.SUNDAY)
		.plus(row.toInt(), DateTimeUnit.WEEK)
		.plus(column.toInt(), DateTimeUnit.DAY)
