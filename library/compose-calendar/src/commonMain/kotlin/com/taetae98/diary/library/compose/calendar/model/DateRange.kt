package com.taetae98.diary.library.compose.calendar.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
public data class DateRange(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
) : ClosedRange<LocalDate>
