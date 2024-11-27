package io.github.taetae98coding.diary.core.navigation.memo

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoAddDestination(
	@SerialName("start")
	val start: LocalDate? = null,
	@SerialName("endInclusive")
	val endInclusive: LocalDate? = null,
	@SerialName(SELECTED_TAG)
	val selectedTag: String? = null,
) {
	public companion object {
		public const val SELECTED_TAG: String = "selectedTag"
	}
}
