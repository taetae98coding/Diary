package io.github.taetae98coding.diary.core.compose.tag.card

import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty

public data class TagCardItemUiState(
	val id: String,
	val title: String,
	val isSelected: Boolean,
	val color: Int,
	val select: SkipProperty<() -> Unit>,
	val unselect: SkipProperty<() -> Unit>,
)
