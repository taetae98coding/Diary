package com.taetae98.diary.data.dto.holiday

import kotlinx.datetime.LocalDate

public data class HolidayDto(
    val name: String,
    override val start: LocalDate,
    override val endInclusive: LocalDate,
) : ClosedRange<LocalDate>