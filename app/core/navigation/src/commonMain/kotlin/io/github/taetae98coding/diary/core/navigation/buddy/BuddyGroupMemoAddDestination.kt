package io.github.taetae98coding.diary.core.navigation.buddy

import io.github.taetae98coding.diary.core.navigation.memo.MemoAddDestination.Companion.SELECTED_TAG
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class BuddyGroupMemoAddDestination(
	@SerialName(GROUP_ID)
	val groupId: String,
	@SerialName(START)
	val start: LocalDate? = null,
	@SerialName(END_INCLUSIVE)
	val endInclusive: LocalDate? = null,
	@SerialName(SELECTED_TAG)
	val selectedTag: String? = null,
) {
	val dateRange: ClosedRange<LocalDate>?
		get() {
			if (start == null) return null
			if (endInclusive == null) return null

			return start..endInclusive
		}

	public companion object {
		public const val GROUP_ID: String = "groupId"
		public const val START: String = "start"
		public const val END_INCLUSIVE: String = "endInclusive"
	}
}
