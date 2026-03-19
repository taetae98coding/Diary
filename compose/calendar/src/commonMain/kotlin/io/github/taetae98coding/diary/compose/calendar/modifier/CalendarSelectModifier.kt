package io.github.taetae98coding.diary.compose.calendar.modifier

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
import io.github.taetae98coding.diary.compose.calendar.CalendarState
import io.github.taetae98coding.diary.compose.calendar.internal.WeekLocalDateRange
import io.github.taetae98coding.diary.compose.calendar.internal.toYearMonth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.minusMonth
import kotlinx.datetime.plus
import kotlinx.datetime.plusMonth

private const val EDGE_THRESHOLD_RATIO = 1F / 14F
private const val AUTO_SCROLL_DELAY_MS = 500L

public fun Modifier.calendarSelect(
    state: CalendarState,
    onSelect: (LocalDateRange) -> Unit,
): Modifier {
    return then(CalendarSelectElement(state, onSelect))
}

private data class CalendarSelectElement(
    val state: CalendarState,
    val onSelect: (LocalDateRange) -> Unit,
) : ModifierNodeElement<CalendarSelectNode>() {
    override fun create(): CalendarSelectNode {
        return CalendarSelectNode(state, onSelect)
    }

    override fun update(node: CalendarSelectNode) {
        node.update(state, onSelect)
    }
}

private class CalendarSelectNode(
    var state: CalendarState,
    var onSelect: (LocalDateRange) -> Unit,
) : DelegatingNode(),
    CompositionLocalConsumerModifierNode {
    private var baseDate: LocalDate? = null
    private var autoScrollJob: Job? = null

    private val pointerInputNode = delegate(
        SuspendingPointerInputModifierNode {
            detectDragGesturesAfterLongPress(
                onDragStart = { offset ->
                    val localDate = offsetToLocalDate(state, offset) ?: return@detectDragGesturesAfterLongPress

                    baseDate = localDate
                    state.selectState.select(localDate..localDate)
                    currentValueOf(LocalHapticFeedback).performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onDragEnd = {
                    autoScrollJob?.cancel()
                    autoScrollJob = null
                    state.selectState.localDateRange?.let(onSelect)
                    baseDate = null
                    state.selectState.unselect()
                },
                onDragCancel = {
                    autoScrollJob?.cancel()
                    autoScrollJob = null
                    baseDate = null
                    state.selectState.unselect()
                },
                onDrag = { change, _ ->
                    val baseDate = baseDate ?: return@detectDragGesturesAfterLongPress
                    val dragDate = offsetToLocalDate(state, change.position) ?: return@detectDragGesturesAfterLongPress
                    val localDateRange = minOf(baseDate, dragDate)..maxOf(baseDate, dragDate)

                    if (localDateRange != state.selectState.localDateRange) {
                        state.selectState.select(localDateRange)
                        currentValueOf(LocalHapticFeedback).performHapticFeedback(HapticFeedbackType.LongPress)
                    }

                    if (autoScrollJob?.isActive != true) {
                        when {
                            change.position.x <= size.width * EDGE_THRESHOLD_RATIO -> {
                                autoScrollJob = coroutineScope.launch {
                                    state.animateScrollToYearMonth(state.yearMonth.minusMonth())
                                    delay(AUTO_SCROLL_DELAY_MS)
                                }
                            }

                            change.position.x >= size.width * (1 - EDGE_THRESHOLD_RATIO) -> {
                                autoScrollJob = coroutineScope.launch {
                                    state.animateScrollToYearMonth(state.yearMonth.plusMonth())
                                    delay(AUTO_SCROLL_DELAY_MS)
                                }
                            }
                        }
                    }
                },
            )
        },
    )

    fun update(
        state: CalendarState,
        onSelect: (LocalDateRange) -> Unit,
    ) {
        this.state = state
        this.onSelect = onSelect
        pointerInputNode.resetPointerInputHandler()
    }
}

private fun PointerInputScope.offsetToLocalDate(
    state: CalendarState,
    offset: Offset,
): LocalDate? {
    if (size.width <= 0 || size.height <= 0) return null

    val weekOfMonth = (offset.y / size.height * 6).toInt().coerceIn(0, 5)
    val dayOfWeek = (offset.x / size.width * 7).toInt().coerceIn(0, 6)

    return WeekLocalDateRange(state.pagerState.currentPage.toYearMonth(), 0)
        .start
        .plus(weekOfMonth, DateTimeUnit.WEEK)
        .plus(dayOfWeek, DateTimeUnit.DAY)
}
