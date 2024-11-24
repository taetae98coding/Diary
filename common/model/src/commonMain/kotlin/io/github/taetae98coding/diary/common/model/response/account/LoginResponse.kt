package io.github.taetae98coding.diary.common.model.response.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class LoginResponse(
	@SerialName("uid")
	val uid: String,
	@SerialName("token")
	val token: String,
)
