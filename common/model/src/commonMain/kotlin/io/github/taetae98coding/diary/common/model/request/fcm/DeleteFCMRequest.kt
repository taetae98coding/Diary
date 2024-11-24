package io.github.taetae98coding.diary.common.model.request.fcm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DeleteFCMRequest(
	@SerialName("token")
	val token: String,
)
