package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@Stable
public class DateRangeCardState(
    initialHasDateRange: Boolean,
    initialIsAllDay: Boolean,
    initialStart: LocalDateTime,
    initialEndInclusive: LocalDateTime,
) {
    public var hasDateRange: Boolean by mutableStateOf(initialHasDateRange)
        private set
    public var isAllDay: Boolean by mutableStateOf(initialIsAllDay)
        private set

    internal var start: LocalDateTime by mutableStateOf(initialStart)
        private set

    internal var endInclusive: LocalDateTime by mutableStateOf(initialEndInclusive)
        private set

    public val localDateRange: ClosedRange<LocalDateTime>?
        get() {
            if (!hasDateRange) return null

            val start = LocalDateTime(start.date, start.time.takeIf { !isAllDay } ?: LocalTime(0, 0))
            val endInclusive = LocalDateTime(endInclusive.date, endInclusive.time.takeIf { !isAllDay } ?: LocalTime(0, 0))

            return start..endInclusive
        }

    public fun updateHasDateRange(value: Boolean) {
        hasDateRange = value
    }

    public fun updateIsAllDay(value: Boolean) {
        isAllDay = value
    }

    public fun updateStart(value: LocalDateTime) {
        start = value
        endInclusive = maxOf(value, endInclusive)
    }

    public fun updateEndInclusive(value: LocalDateTime) {
        start = minOf(start, value)
        endInclusive = value
    }

    public companion object {
        public fun saver(): Saver<DateRangeCardState, Any> = listSaver(
            save = {
                listOf(
                    it.hasDateRange,
                    it.isAllDay,
                    it.start.toString(),
                    it.endInclusive.toString(),
                )
            },
            restore = {
                DateRangeCardState(
                    initialHasDateRange = it[0] as Boolean,
                    initialIsAllDay = it[1] as Boolean,
                    initialStart = LocalDateTime.parse(it[2] as String),
                    initialEndInclusive = LocalDateTime.parse(it[3] as String),
                )
            },
        )
    }
}

@Composable
public fun rememberDateRangeCardState(
    vararg inputs: Any?,
    initialHasDateRange: Boolean = false,
    initialIsAllDay: Boolean = true,
    initialStart: LocalDateTime = remember {
        val localDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        LocalDateTime(localDateTime.date, LocalTime(localDateTime.hour, localDateTime.minute / 10 * 10))
    },
    initialEndInclusive: LocalDateTime = remember {
        initialStart
            .toInstant(TimeZone.UTC)
            .plus(1.hours)
            .toLocalDateTime(TimeZone.UTC)
    },
): DateRangeCardState {
    return rememberSaveable(
        inputs = inputs,
        saver = DateRangeCardState.saver(),
    ) {
        DateRangeCardState(
            initialHasDateRange = initialHasDateRange,
            initialIsAllDay = initialIsAllDay,
            initialStart = initialStart,
            initialEndInclusive = initialEndInclusive,
        )
    }
}
