package io.github.taetae98coding.diary.core.compose.tag.memo

public data class TagMemoScaffoldUiState(
	val isFinish: Boolean = false,
	val isDelete: Boolean = false,
	val isUnknownError: Boolean = false,
	val restartTag: () -> Unit = {},
	val restoreTag: () -> Unit = {},
	val clearState: () -> Unit = {},
)
