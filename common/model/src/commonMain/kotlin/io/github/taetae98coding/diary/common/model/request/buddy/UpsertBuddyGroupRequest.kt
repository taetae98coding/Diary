package io.github.taetae98coding.diary.common.model.request.buddy

import io.github.taetae98coding.diary.common.model.buddy.BuddyGroupEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class UpsertBuddyGroupRequest(
	@SerialName("buddyGroup")
	val buddyGroup: BuddyGroupEntity,
	@SerialName("buddyIds")
	val buddyIds: Set<String>,
)
