package io.github.taetae98coding.diary.presenter.calendar.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import io.github.taetae98coding.diary.compose.calendar.Calendar
import io.github.taetae98coding.diary.compose.calendar.item.CALENDAR_COLOR_LABEL_TEXT_CONTENT_TYPE
import io.github.taetae98coding.diary.compose.calendar.item.CALENDAR_TEXT_CONTENT_TYPE
import io.github.taetae98coding.diary.compose.calendar.item.CalendarColorLabelText
import io.github.taetae98coding.diary.compose.calendar.item.CalendarText
import io.github.taetae98coding.diary.compose.calendar.modifier.calendarSelect
import io.github.taetae98coding.diary.compose.core.dialog.LocalDatePickerDialogHost
import io.github.taetae98coding.diary.compose.core.icon.DropDownIcon
import io.github.taetae98coding.diary.compose.core.icon.DropUpIcon
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import io.github.taetae98coding.diary.presenter.calendar.api.CalendarMemoStateHolder
import kotlin.time.Clock
import kotlin.uuid.Uuid
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minusMonth
import kotlinx.datetime.number
import kotlinx.datetime.plusMonth
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth

@Composable
public fun CalendarScaffold(
    memoStateHolder: CalendarMemoStateHolder,
    modifier: Modifier = Modifier,
    isFetchingProvider: () -> Boolean = { false },
    onFetch: () -> Unit = {},
    onLocalDateRangeSelect: (LocalDateRange) -> Unit = {},
    onMemoClick: (Uuid) -> Unit = {},
) {
    val memoList by memoStateHolder.calendarMemo.collectAsStateWithLifecycle()
    val state = rememberCalendarScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    CalendarScaffold(
        modifier = modifier,
        state = state,
        isFetchingProvider = isFetchingProvider,
        onFetch = onFetch,
        memoListProvider = { memoList },
        onLocalDateRangeSelect = onLocalDateRangeSelect,
        onMemoClick = onMemoClick,
    )

    LocalDatePickerDialogHost(
        state = state.localDatePickerDialogState,
        localDateProvider = { state.calendarState.yearMonth.firstDay },
        onSelect = { coroutineScope.launch { state.calendarState.animateScrollToYearMonth(it.yearMonth) } },
    )

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        state.updatePrimaryDate(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    }

    LifecycleStartEffect(state.calendarState.yearMonth) {
        memoStateHolder.fetch(state.calendarState.yearMonth)
        onStopOrDispose { }
    }
}

@Composable
public fun CalendarScaffold(
    modifier: Modifier = Modifier,
    state: CalendarScaffoldState = rememberCalendarScaffoldState(),
    isFetchingProvider: () -> Boolean = { false },
    onFetch: () -> Unit = {},
    memoListProvider: () -> List<CalendarMemo>,
    onLocalDateRangeSelect: (LocalDateRange) -> Unit = {},
    onMemoClick: (Uuid) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = { TopBar(state = state) },
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isFetchingProvider(),
            onRefresh = onFetch,
            modifier = Modifier.padding(paddingValues),
        ) {
            Calendar(
                state = state.calendarState,
                modifier = Modifier
                    .calendarSelect(
                        state = state.calendarState,
                        onSelect = onLocalDateRangeSelect,
                    )
                    .focusableKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown) {
                            when (event.key) {
                                Key.DirectionLeft -> {
                                    coroutineScope.launch { state.calendarState.animateScrollToYearMonth(state.calendarState.yearMonth.minusMonth()) }
                                    true
                                }

                                Key.DirectionRight -> {
                                    coroutineScope.launch { state.calendarState.animateScrollToYearMonth(state.calendarState.yearMonth.plusMonth()) }
                                    true
                                }

                                else -> false
                            }
                        } else {
                            false
                        }
                    },
                primaryDayListProvider = { listOfNotNull(state.primaryDate) },
            ) {
                items(
                    items = memoListProvider().filter { !(!it.isAllDay && it.localDateTimeRange.start.date == it.localDateTimeRange.endInclusive.date) },
                    key = { it.id.toString() },
                    contentType = { CALENDAR_TEXT_CONTENT_TYPE },
                    localDateRange = { it.localDateTimeRange.start.date..it.localDateTimeRange.endInclusive.date },
                ) {
                    CalendarText(
                        text = it.title,
                        color = Color(it.color),
                        modifier = Modifier.animateItem()
                            .clickable(onClick = dropUnlessResumed { onMemoClick(it.id) }),
                    )
                }

                items(
                    items = memoListProvider().filter { !it.isAllDay && it.localDateTimeRange.start.date == it.localDateTimeRange.endInclusive.date },
                    key = { it.id.toString() },
                    contentType = { CALENDAR_COLOR_LABEL_TEXT_CONTENT_TYPE },
                    localDateRange = { it.localDateTimeRange.start.date..it.localDateTimeRange.endInclusive.date },
                ) {
                    CalendarColorLabelText(
                        text = it.title,
                        color = Color(it.color),
                        modifier = Modifier.animateItem()
                            .clickable { onMemoClick(it.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    state: CalendarScaffoldState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = state.localDatePickerDialogState::show)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "${state.calendarState.yearMonth.year}년 ${state.calendarState.yearMonth.month.number}월")
                Crossfade(targetState = state.localDatePickerDialogState.isVisible) { isVisible ->
                    if (isVisible) {
                        DropUpIcon()
                    } else {
                        DropDownIcon()
                    }
                }
            }
        },
        modifier = modifier,
        actions = {
            state.primaryDate?.let {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { coroutineScope.launch { state.calendarState.animateScrollToYearMonth(it.yearMonth) } }
                        .size(40.dp)
                        .padding(6.dp)
                        .border(1.dp, DiaryTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = it.day.toString(),
                        style = DiaryTheme.typography.labelMedium,
                    )
                }
            }
        },
    )
}

@ScreenPreview
@Composable
private fun Preview() {
    val state = rememberCalendarScaffoldState(
        initialYearMonth = YearMonth(1998, 1),
        initialPrimaryDate = LocalDate(1998, 1, 9),
    )

    DiaryTheme {
        CalendarScaffold(
            state = state,
            memoListProvider = { emptyList() },
            onLocalDateRangeSelect = {},
        )
    }
}
