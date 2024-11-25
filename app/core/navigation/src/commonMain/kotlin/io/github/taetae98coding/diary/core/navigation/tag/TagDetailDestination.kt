package io.github.taetae98coding.diary.core.navigation.tag

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TagDetailDestination(
	@SerialName(TAG_ID)
	val tagId: String,
) {
	public companion object {
		public const val TAG_ID: String = "tagId"
	}
}
