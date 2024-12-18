package io.github.taetae98coding.diary.core.compose.memo.detail

public data class MemoDetailScaffoldUiState(
	val isProgress: Boolean = false,
	val isAdd: Boolean = false,
	val isDelete: Boolean = false,
	val isUpdate: Boolean = false,
	val isTitleBlankError: Boolean = false,
	val isNetworkError: Boolean = false,
	val isUnknownError: Boolean = false,
	val onMessageShow: () -> Unit = {},
)
