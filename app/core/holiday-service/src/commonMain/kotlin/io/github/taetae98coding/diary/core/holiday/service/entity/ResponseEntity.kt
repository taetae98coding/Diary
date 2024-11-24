package io.github.taetae98coding.diary.core.holiday.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseEntity(
	@SerialName("body")
	val body: BodyEntity,
)
