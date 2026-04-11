package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.dialog.LocalDatePickerDialogHost
import io.github.taetae98coding.diary.compose.core.dialog.LocalTimePickerDialogHost
import io.github.taetae98coding.diary.compose.core.dialog.rememberDialogState
import io.github.taetae98coding.diary.compose.core.icon.ScheduleIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.Padding

@Composable
public fun DateRangeCard(
    modifier: Modifier = Modifier,
    state: DateRangeCardState = rememberDateRangeCardState(),
) {
    Card(modifier = modifier) {
        DateRangeTitle(state = state)
        AnimatedVisibility(visible = state.hasDateRange) {
            DateRangePicker(state = state)
        }
    }
}

@Composable
private fun DateRangeTitle(
    state: DateRangeCardState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(value = state.hasDateRange, onValueChange = state::updateHasDateRange)
            .padding(horizontal = 16.dp)
            .minimumInteractiveComponentSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "날짜 범위",
            style = DiaryTheme.typography.titleMedium,
        )
        Switch(
            checked = state.hasDateRange,
            onCheckedChange = null,
        )
    }
}

@Composable
private fun DateRangePicker(
    state: DateRangeCardState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DateRangeIsAllDay(state = state)
        Row {
            LocalDateButton(
                localDateProvider = state.start::date,
                onLocalDateChange = { state.updateStart(LocalDateTime(it, state.start.time)) },
                modifier = Modifier.weight(1F),
            )
            LocalDateButton(
                localDateProvider = state.endInclusive::date,
                onLocalDateChange = { state.updateEndInclusive(LocalDateTime(it, state.endInclusive.time)) },
                modifier = Modifier.weight(1F),
            )
        }
        AnimatedVisibility(visible = !state.isAllDay) {
            Row {
                LocalTimeButton(
                    localTimeProvider = state.start::time,
                    onLocalTimeChange = { state.updateStart(LocalDateTime(state.start.date, it)) },
                    modifier = Modifier.weight(1F),
                )
                LocalTimeButton(
                    localTimeProvider = state.endInclusive::time,
                    onLocalTimeChange = { state.updateEndInclusive(LocalDateTime(state.endInclusive.date, it)) },
                    modifier = Modifier.weight(1F),
                )
            }
        }
    }
}

@Composable
private fun DateRangeIsAllDay(
    state: DateRangeCardState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(value = state.isAllDay, onValueChange = state::updateIsAllDay)
            .padding(horizontal = 16.dp)
            .minimumInteractiveComponentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ScheduleIcon()
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "종일")
        Spacer(modifier = Modifier.weight(1F))
        Switch(
            checked = state.isAllDay,
            onCheckedChange = null,
        )
    }
}

@Composable
private fun LocalDateButton(
    localDateProvider: () -> LocalDate,
    onLocalDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dialogState = rememberDialogState()

    LocalDateButton(
        localDateProvider = localDateProvider,
        onClick = dialogState::show,
        modifier = modifier,
    )

    LocalDatePickerDialogHost(
        state = dialogState,
        localDateProvider = localDateProvider,
        onSelect = onLocalDateChange,
    )
}

@Composable
private fun LocalTimeButton(
    localTimeProvider: () -> LocalTime,
    onLocalTimeChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dialogState = rememberDialogState()

    LocalTimeButton(
        localTimeProvider = localTimeProvider,
        onClick = dialogState::show,
        modifier = modifier,
    )

    LocalTimePickerDialogHost(
        state = dialogState,
        localTimeProvider = localTimeProvider,
        onSelect = onLocalTimeChange,
    )
}

@Composable
private fun LocalDateButton(
    localDateProvider: () -> LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        val localDate = localDateProvider()
        val format = remember {
            LocalDate.Format {
                monthNumber(padding = Padding.SPACE)
                chars(value = "월")

                chars(value = " ")

                day(padding = Padding.SPACE)
                chars(value = "일")

                chars(value = " ")

                dayOfWeek(DayOfWeekNames(listOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")))
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedContent(
                targetState = localDate.year,
                transitionSpec = transitionSpec(),
            ) { year ->
                Text(
                    text = "${year}년",
                    style = DiaryTheme.typography.labelSmall,
                )
            }
            AnimatedContent(
                targetState = localDate,
                transitionSpec = transitionSpec(),
            ) { localDate ->
                Text(
                    text = remember(localDate, format) { localDate.format(format) },
                    style = DiaryTheme.typography.labelLargeEmphasized,
                )
            }
        }
    }
}

@Composable
private fun LocalTimeButton(
    localTimeProvider: () -> LocalTime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        val localTime = localTimeProvider()
        val amPmFormat = remember {
            LocalTime.Format {
                amPmMarker(am = "오전", pm = "오후")
            }
        }
        val hourMinuteFormat = remember {
            LocalTime.Format {
                amPmHour(padding = Padding.SPACE)
                chars(value = "시")

                chars(value = " ")

                minute(padding = Padding.SPACE)
                chars(value = "분")
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = remember(localTime, amPmFormat) { localTime.format(amPmFormat) },
                style = DiaryTheme.typography.labelSmall,
            )
            AnimatedContent(
                targetState = localTime,
                transitionSpec = transitionSpec(),
            ) { localTime ->
                Text(
                    text = remember(localTime, hourMinuteFormat) { localTime.format(hourMinuteFormat) },
                    style = DiaryTheme.typography.labelLargeEmphasized,
                )
            }
        }
    }
}

private fun <T : Comparable<T>> transitionSpec(): AnimatedContentTransitionScope<T>.() -> ContentTransform {
    return {
        if (targetState > initialState) {
            val inAnimation = slideInVertically { height -> height } + fadeIn()
            val outAnimation = slideOutVertically { height -> -height } + fadeOut()

            inAnimation togetherWith outAnimation
        } else {
            val inAnimation = slideInVertically { height -> -height } + fadeIn()
            val outAnimation = slideOutVertically { height -> height } + fadeOut()

            inAnimation togetherWith outAnimation
        }.using(
            SizeTransform(clip = false),
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        DateRangeCard()
    }
}
