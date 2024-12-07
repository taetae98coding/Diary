package io.github.taetae98coding.diary.common.model.buddy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class BuddyEntity(
	@SerialName("uid")
	val uid: String,
	@SerialName("email")
	val email: String,
)
