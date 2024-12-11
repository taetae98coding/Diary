package io.github.taetae98coding.diary.core.compose.tag.list

import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty

public data class TagListItemUiState(
	val id: String,
	val title: String,
	val finish: SkipProperty<() -> Unit>,
	val delete: SkipProperty<() -> Unit>,
)
