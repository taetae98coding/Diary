package io.github.taetae98coding.diary.core.navigation.buddy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class BuddyGroupCalendarDestination(
	@SerialName(GROUP_ID)
	val groupId: String,
) {
	public companion object {
		public const val GROUP_ID: String = "GROUP_ID"
	}
}
