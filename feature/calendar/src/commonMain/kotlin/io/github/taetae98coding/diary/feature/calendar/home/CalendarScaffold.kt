package io.github.taetae98coding.diary.feature.calendar.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import coil3.compose.AsyncImage
import io.github.taetae98coding.diary.compose.calendar.Calendar
import io.github.taetae98coding.diary.compose.calendar.CalendarDefaults
import io.github.taetae98coding.diary.compose.calendar.item.CALENDAR_COLOR_LABEL_TEXT_CONTENT_TYPE
import io.github.taetae98coding.diary.compose.calendar.item.CALENDAR_TEXT_CONTENT_TYPE
import io.github.taetae98coding.diary.compose.calendar.item.CalendarColorLabelText
import io.github.taetae98coding.diary.compose.calendar.item.CalendarText
import io.github.taetae98coding.diary.compose.calendar.modifier.calendarSelect
import io.github.taetae98coding.diary.compose.core.icon.DropDownIcon
import io.github.taetae98coding.diary.compose.core.icon.DropUpIcon
import io.github.taetae98coding.diary.compose.core.icon.FilterListIcon
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import io.github.taetae98coding.diary.core.model.routine.CalendarRoutine
import kotlin.math.roundToInt
import kotlin.uuid.Uuid
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minusMonth
import kotlinx.datetime.number
import kotlinx.datetime.plusMonth
import kotlinx.datetime.yearMonth

@Composable
internal fun CalendarScaffold(
    modifier: Modifier = Modifier,
    state: CalendarScaffoldState = rememberCalendarScaffoldState(),
    isFetchingProvider: () -> Boolean = { false },
    hasFilterProvider: () -> Boolean = { false },
    memoListProvider: () -> List<CalendarMemo> = { emptyList() },
    routineListProvider: () -> List<CalendarRoutine> = { emptyList() },
    holidayListProvider: () -> List<Holiday> = { emptyList() },
    weatherListProvider: () -> List<CalendarWeatherUiState> = { emptyList() },
    onFetch: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onLocalDateRangeSelect: (LocalDateRange) -> Unit = {},
    onMemoClick: (Uuid) -> Unit = {},
    onRoutineClick: (Uuid) -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                state = state,
                onFilterClick = onFilterClick,
                hasFilterProvider = hasFilterProvider,
            )
        },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isFetchingProvider(),
            onRefresh = onFetch,
            modifier = Modifier.padding(paddingValues),
        ) {
            val calendarColors = CalendarDefaults.colors()

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
                holidayListProvider = { holidayListProvider().filter(Holiday::isHoliday).map(Holiday::localDateRange) },
                colors = calendarColors,
            ) {
                items(
                    items = weatherListProvider(),
                    localDateRange = { it.localDate..it.localDate },
                ) {
                    WeatherItem(uiStateProvider = { it })
                }

                appendLine()

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

                items(
                    items = routineListProvider(),
                    key = { "${it.id}-${it.occurrence}" },
                    contentType = { CALENDAR_TEXT_CONTENT_TYPE },
                    localDateRange = { it.localDateRange },
                ) {
                    CalendarText(
                        text = it.title,
                        color = Color(it.color),
                        modifier = Modifier.animateItem()
                            .clickable(onClick = dropUnlessResumed { onRoutineClick(it.id) }),
                    )
                }

                items(
                    items = holidayListProvider(),
                    key = { it.toString() },
                    contentType = { CALENDAR_TEXT_CONTENT_TYPE },
                    localDateRange = { it.localDateRange },
                ) {
                    CalendarText(
                        text = it.name,
                        color = if (it.isHoliday) {
                            calendarColors.sundayColor
                        } else {
                            DiaryTheme.colorScheme.secondary
                        },
                        modifier = Modifier.animateItem()
                            .clickable { uriHandler.openUri(it.link) },
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
    hasFilterProvider: () -> Boolean = { false },
    onFilterClick: () -> Unit = {},
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
            IconButton(
                onClick = dropUnlessResumed(block = onFilterClick),
                colors = if (hasFilterProvider()) {
                    IconButtonDefaults.iconButtonColors(contentColor = DiaryTheme.colorScheme.primary)
                } else {
                    IconButtonDefaults.iconButtonColors()
                },
            ) {
                FilterListIcon()
            }
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

@Composable
private fun WeatherItem(
    uiStateProvider: () -> CalendarWeatherUiState,
    modifier: Modifier = Modifier,
) {
    val uiState = uiStateProvider()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            uiState.typeList.forEach { type ->
                AsyncImage(
                    model = type.icon,
                    contentDescription = type.description,
                    modifier = Modifier.height(32.dp),
                    contentScale = ContentScale.FillHeight,
                )
            }
        }

        val text = if (uiState.isToday) {
            "${uiState.temperature.roundToInt()}°"
        } else {
            listOfNotNull(
                uiState.minTemperature,
                uiState.maxTemperature,
            ).joinToString(separator = "/") {
                "${it.roundToInt()}°"
            }
        }

        Text(
            text = text,
            color = DiaryTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = DiaryTheme.typography.labelSmall,
        )
    }
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
