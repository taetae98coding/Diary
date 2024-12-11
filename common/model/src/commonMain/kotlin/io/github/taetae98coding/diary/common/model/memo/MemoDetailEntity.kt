package io.github.taetae98coding.diary.common.model.memo

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoDetailEntity(
	@SerialName("title")
	val title: String,
	@SerialName("description")
	val description: String,
	@SerialName("start")
	val start: LocalDate?,
	@SerialName("endInclusive")
	val endInclusive: LocalDate?,
	@SerialName("color")
	val color: Int,
)
