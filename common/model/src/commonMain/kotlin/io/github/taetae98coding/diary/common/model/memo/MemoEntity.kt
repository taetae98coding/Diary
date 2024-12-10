package io.github.taetae98coding.diary.common.model.memo

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoEntity(
	@SerialName("id")
	val id: String = "",
	@SerialName("title")
	val title: String = "",
	@SerialName("description")
	val description: String = "",
	@SerialName("start")
	val start: LocalDate? = null,
	@SerialName("endInclusive")
	val endInclusive: LocalDate? = null,
	@SerialName("color")
	val color: Int = -16777216,
	@SerialName("primaryTag")
	val primaryTag: String? = null,
	@SerialName("isFinish")
	val isFinish: Boolean = false,
	@SerialName("isDelete")
	val isDelete: Boolean = false,
	@SerialName("updateAt")
	val updateAt: Instant = Instant.fromEpochMilliseconds(0L),
)
