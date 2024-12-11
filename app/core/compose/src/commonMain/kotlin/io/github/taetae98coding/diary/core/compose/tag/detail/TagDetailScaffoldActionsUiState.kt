package io.github.taetae98coding.diary.core.compose.tag.detail

public data class TagDetailScaffoldActionsUiState(
	val isFinish: Boolean = false,
	val finish: () -> Unit = {},
	val restart: () -> Unit = {},
	val delete: () -> Unit = {},
)
