package io.github.taetae98coding.diary.core.model.holiday

import kotlinx.datetime.LocalDate

public data class Holiday(
	val name: String,
	override val start: LocalDate,
	override val endInclusive: LocalDate,
) : ClosedRange<LocalDate>
