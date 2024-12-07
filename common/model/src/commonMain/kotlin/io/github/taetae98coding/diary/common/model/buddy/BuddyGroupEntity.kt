package io.github.taetae98coding.diary.common.model.buddy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class BuddyGroupEntity(
	@SerialName("id")
	val id: String,
	@SerialName("title")
	val title: String,
	@SerialName("description")
	val description: String,
)
