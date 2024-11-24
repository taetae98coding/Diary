package io.github.taetae98coding.diary.core.holiday.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiResultEntity(
	@SerialName("response")
	val response: ResponseEntity,
)
