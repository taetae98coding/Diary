package com.taetae98.diary.library.compose.calendar.model

import kotlinx.datetime.LocalDate

public data class DateRange(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
) : ClosedRange<LocalDate>
