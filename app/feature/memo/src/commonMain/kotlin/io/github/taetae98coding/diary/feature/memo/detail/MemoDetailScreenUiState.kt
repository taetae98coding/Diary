package io.github.taetae98coding.diary.feature.memo.detail

internal data class MemoDetailScreenUiState(val isProgress: Boolean = false, val isAdd: Boolean = false, val isDelete: Boolean = false, val isUpdate: Boolean = false, val isTitleBlankError: Boolean = false, val isUnknownError: Boolean = false, val onMessageShow: () -> Unit = {}) {
	val hasMessage = isAdd || isDelete || isUpdate || isTitleBlankError || isUnknownError
}
