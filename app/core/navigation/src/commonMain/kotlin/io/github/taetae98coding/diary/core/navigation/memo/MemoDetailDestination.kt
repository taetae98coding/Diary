package io.github.taetae98coding.diary.core.navigation.memo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoDetailDestination(
	@SerialName(MEMO_ID)
	val memoId: String,
) {
	public companion object {
		public const val MEMO_ID: String = "memoId"
	}
}
