package io.github.taetae98coding.diary.core.compose.tag.list

import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty

public data class TagListScaffoldUiState(
	val isTagFinish: Boolean = false,
	val isTagDelete: Boolean = false,
	val isNetworkError: Boolean = false,
	val isUnknownError: Boolean = false,
	val restart: SkipProperty<() -> Unit> = SkipProperty {},
	val restore: SkipProperty<() -> Unit> = SkipProperty {},
	val clearState: () -> Unit = {},
)
