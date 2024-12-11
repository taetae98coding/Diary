package io.github.taetae98coding.diary.core.compose.tag.detail

public data class TagDetailActionsUiState(
	val isFinish: Boolean,
	val finish: () -> Unit,
	val restart: () -> Unit,
	val delete: () -> Unit,
)
