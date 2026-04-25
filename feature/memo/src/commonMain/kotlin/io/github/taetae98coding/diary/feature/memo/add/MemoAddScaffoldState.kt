package io.github.taetae98coding.diary.feature.memo.add

import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.compose.core.card.ColorCardState
import io.github.taetae98coding.diary.compose.core.card.DateRangeCardState
import io.github.taetae98coding.diary.compose.core.card.DescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.TitleCardState
import io.github.taetae98coding.diary.compose.core.card.rememberColorCardState
import io.github.taetae98coding.diary.compose.core.card.rememberDateRangeCardState
import io.github.taetae98coding.diary.compose.core.card.rememberDescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.rememberTitleCardState
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.library.compose.ui.random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

internal class MemoAddScaffoldState(
    val titleCardState: TitleCardState,
    val descriptionCardState: DescriptionCardState,
    val dateRangeCardState: DateRangeCardState,
    val colorCardState: ColorCardState,
) {
    val hostState: SnackbarHostState = SnackbarHostState()

    val detail: MemoDetail
        get() = MemoDetail(
            title = titleCardState.textFieldState.text.toString(),
            description = descriptionCardState.textFieldState.text.toString(),
            isAllDay = dateRangeCardState.isAllDay,
            localDateTimeRange = dateRangeCardState.localDateRange,
            color = colorCardState.value.toArgb(),
        )

    suspend fun reset() {
        coroutineScope {
            launch { colorCardState.updateColor(Color.random()) }
            titleCardState.textFieldState.clearText()
            descriptionCardState.textFieldState.clearText()
        }
    }
}

@Composable
internal fun rememberMemoAddScaffoldState(): MemoAddScaffoldState {
    return rememberMemoAddScaffoldState(initialLocalDateRange = null)
}

@Composable
internal fun rememberMemoAddScaffoldState(initialLocalDateRange: LocalDateRange?): MemoAddScaffoldState {
    val startLocalDate = remember { initialLocalDateRange?.start ?: Clock.System.todayIn(TimeZone.currentSystemDefault()) }
    val startLocalTime = remember {
        val localTime = Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .time
        LocalTime(localTime.hour, localTime.minute / 10 * 10)
    }
    val endInclusiveLocalDate = remember { initialLocalDateRange?.endInclusive ?: startLocalDate }
    val endInclusiveLocalTime = remember {
        if (startLocalDate == endInclusiveLocalDate) {
            LocalDateTime(endInclusiveLocalDate, startLocalTime)
                .toInstant(TimeZone.UTC)
                .plus(1.hours)
                .toLocalDateTime(TimeZone.UTC)
                .time
        } else {
            startLocalTime
        }
    }
    val titleCardState = rememberTitleCardState()
    val descriptionCardState = rememberDescriptionCardState()
    val dateRangeCardState = rememberDateRangeCardState(
        initialHasDateRange = initialLocalDateRange != null,
        initialIsAllDay = true,
        initialStart = LocalDateTime(startLocalDate, startLocalTime),
        initialEndInclusive = LocalDateTime(endInclusiveLocalDate, endInclusiveLocalTime),
    )
    val colorCardState = rememberColorCardState()

    return retain(titleCardState, descriptionCardState, dateRangeCardState, colorCardState) {
        MemoAddScaffoldState(
            titleCardState = titleCardState,
            descriptionCardState = descriptionCardState,
            dateRangeCardState = dateRangeCardState,
            colorCardState = colorCardState,
        )
    }
}
