package io.github.taetae98coding.diary.core.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

public data class Memo(
	val id: String,
	val title: String,
	val description: String,
	val start: LocalDate?,
	val endInclusive: LocalDate?,
	val color: Int,
	val owner: String,
	val isFinish: Boolean,
	val isDelete: Boolean,
	val updateAt: Instant,
)
