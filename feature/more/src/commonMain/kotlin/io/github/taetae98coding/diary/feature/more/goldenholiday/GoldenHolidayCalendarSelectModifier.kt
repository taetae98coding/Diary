package io.github.taetae98coding.diary.feature.more.goldenholiday

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.LocalHapticFeedback
import io.github.taetae98coding.diary.compose.calendar.CalendarSelectState
import io.github.taetae98coding.diary.compose.calendar.internal.WeekLocalDateRange
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth
import kotlinx.datetime.plus

internal fun Modifier.goldenHolidayCalendarSelect(
    yearMonth: YearMonth,
    weekOfMonthList: List<Int>,
    selectState: CalendarSelectState,
    onSelect: (LocalDateRange) -> Unit,
): Modifier {
    return then(GoldenHolidayCalendarSelectElement(yearMonth, weekOfMonthList, selectState, onSelect))
}

private data class GoldenHolidayCalendarSelectElement(
    val yearMonth: YearMonth,
    val weekOfMonthList: List<Int>,
    val selectState: CalendarSelectState,
    val onSelect: (LocalDateRange) -> Unit,
) : ModifierNodeElement<GoldenHolidayCalendarSelectNode>() {
    override fun create(): GoldenHolidayCalendarSelectNode {
        return GoldenHolidayCalendarSelectNode(yearMonth, weekOfMonthList, selectState, onSelect)
    }

    override fun update(node: GoldenHolidayCalendarSelectNode) {
        node.update(yearMonth, weekOfMonthList, selectState, onSelect)
    }
}

private class GoldenHolidayCalendarSelectNode(
    var yearMonth: YearMonth,
    var weekOfMonthList: List<Int>,
    var selectState: CalendarSelectState,
    var onSelect: (LocalDateRange) -> Unit,
) : DelegatingNode(),
    CompositionLocalConsumerModifierNode {
    private var baseDate: LocalDate? = null

    private val pointerInputNode = delegate(
        SuspendingPointerInputModifierNode {
            detectDragGesturesAfterLongPress(
                onDragStart = { offset ->
                    val localDate = offsetToLocalDate(offset) ?: return@detectDragGesturesAfterLongPress

                    baseDate = localDate
                    selectState.select(localDate..localDate)
                    currentValueOf(LocalHapticFeedback).performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onDragEnd = {
                    selectState.localDateRange?.let(onSelect)
                    baseDate = null
                    selectState.unselect()
                },
                onDragCancel = {
                    baseDate = null
                    selectState.unselect()
                },
                onDrag = { change, _ ->
                    val base = baseDate ?: return@detectDragGesturesAfterLongPress
                    val dragDate = offsetToLocalDate(change.position) ?: return@detectDragGesturesAfterLongPress
                    val localDateRange = minOf(base, dragDate)..maxOf(base, dragDate)

                    if (localDateRange != selectState.localDateRange) {
                        selectState.select(localDateRange)
                        currentValueOf(LocalHapticFeedback).performHapticFeedback(HapticFeedbackType.SegmentTick)
                    }
                },
            )
        },
    )

    private fun PointerInputScope.offsetToLocalDate(offset: Offset): LocalDate? {
        if (weekOfMonthList.isEmpty() || size.width <= 0 || size.height <= 0) return null

        val weekHeight = size.height.toFloat() / weekOfMonthList.size
        val weekIndex = (offset.y / weekHeight).toInt().coerceIn(0, weekOfMonthList.lastIndex)
        val weekOfMonth = weekOfMonthList[weekIndex]
        val dayOfWeek = (offset.x / size.width * 7).toInt().coerceIn(0, 6)

        return WeekLocalDateRange(yearMonth, weekOfMonth).start.plus(dayOfWeek, DateTimeUnit.DAY)
    }

    fun update(
        yearMonth: YearMonth,
        weekOfMonthList: List<Int>,
        selectState: CalendarSelectState,
        onSelect: (LocalDateRange) -> Unit,
    ) {
        this.yearMonth = yearMonth
        this.weekOfMonthList = weekOfMonthList
        this.selectState = selectState
        this.onSelect = onSelect
        pointerInputNode.resetPointerInputHandler()
    }
}
