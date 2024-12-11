package io.github.taetae98coding.diary.core.compose.memo.detail

public data class MemoDetailScaffoldActionsUiState(
	val isFinish: Boolean = false,
	val finish: () -> Unit = {},
	val restart: () -> Unit = {},
	val delete: () -> Unit = {},
)
