package io.github.taetae98coding.diary.core.calendar.compose.item

import kotlinx.datetime.LocalDate

public sealed class CalendarItemUiState : ClosedRange<LocalDate>, Comparable<CalendarItemUiState> {
    public abstract val key: Any
    public abstract val text: String

    public data class Holiday(
        override val text: String,
        override val start: LocalDate,
        override val endInclusive: LocalDate,
    ) : CalendarItemUiState() {
        override val key: String = "$text($start~$endInclusive)"
    }

    public data class Text(
        override val key: Any,
        override val text: String,
        val color: Int,
        override val start: LocalDate,
        override val endInclusive: LocalDate,
    ) : CalendarItemUiState()

    override fun compareTo(other: CalendarItemUiState): Int {
        if (start != other.start) {
            return compareValues(start, other.start)
        }

        if (endInclusive != other.endInclusive) {
            return -compareValues(endInclusive, other.endInclusive)
        }

        return compareValues(text, other.text)
    }
}
