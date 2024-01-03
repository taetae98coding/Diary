package com.taetae98.diary.domain.entity.account.holiday

import kotlinx.datetime.LocalDate

public data class Holiday(
    val name: String,
    override val start: LocalDate,
    override val endInclusive: LocalDate,
) : ClosedRange<LocalDate>
