package io.github.taetae98coding.diary.feature.tag.memo

internal data class TagMemoScreenUiState(
	val finishTagId: String? = null,
	val deleteTagId: String? = null,
	val isUnknownError: Boolean = false,
	val restartTag: (String) -> Unit = {},
	val restoreTag: (String) -> Unit = {},
	val onMessageShow: () -> Unit = {},
) {
	val hasMessage = !finishTagId.isNullOrBlank() || !deleteTagId.isNullOrBlank() || isUnknownError
}
