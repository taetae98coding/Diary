package com.taetae98.diary.library.compose.calendar

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
public sealed class CalendarItem : ClosedRange<LocalDate> {
    public abstract val key: Any
    public abstract val name: String

    public data class Holiday(
        override val name: String,
        override val start: LocalDate,
        override val endInclusive: LocalDate,
    ) : CalendarItem() {
        override val key: Any = toString()
    }
}
