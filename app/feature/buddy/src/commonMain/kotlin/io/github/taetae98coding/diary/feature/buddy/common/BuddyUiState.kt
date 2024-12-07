package io.github.taetae98coding.diary.feature.buddy.common

import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty

internal data class BuddyUiState(
	val uid: String,
	val email: String,
	val isSelected: Boolean,
	val select: SkipProperty<() -> Unit>,
	val unselect: SkipProperty<() -> Unit>,
)
