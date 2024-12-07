package io.github.taetae98coding.diary.feature.buddy.detail

internal data class BuddyDetailScreenUiState(
	val isProgress: Boolean = false,
	val isAdd: Boolean = false,
	val isExit: Boolean = false,
	val isUpdate: Boolean = false,
	val isTitleBlankError: Boolean = false,
	val isUnknownError: Boolean = false,
	val isNetworkError: Boolean = false,
	val onMessageShow: () -> Unit,
) {
	val hasMessage = isAdd || isExit || isUpdate || isTitleBlankError || isNetworkError || isUnknownError
}
