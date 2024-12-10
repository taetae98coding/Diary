package io.github.taetae98coding.diary.core.navigation.buddy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class BuddyGroupMemoDetailDestination(
	@SerialName(MEMO_ID)
	val memoId: String,
) {
	public companion object {
		public const val MEMO_ID: String = "memoId"
	}
}
