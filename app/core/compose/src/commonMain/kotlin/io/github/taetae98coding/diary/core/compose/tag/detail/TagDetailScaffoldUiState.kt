package io.github.taetae98coding.diary.core.compose.tag.detail

import io.github.taetae98coding.diary.core.model.tag.TagDetail

public sealed class TagDetailScaffoldUiState {
	public abstract val isTitleBlankError: Boolean
	public abstract val isNetworkError: Boolean
	public abstract val isUnknownError: Boolean
	public abstract val clearState: () -> Unit

	public data class Add(
		val isAddInProgress: Boolean = false,
		val isAddFinish: Boolean = false,
		override val isTitleBlankError: Boolean = false,
		override val isNetworkError: Boolean = false,
		override val isUnknownError: Boolean = false,
		val add: (TagDetail) -> Unit = {},
		override val clearState: () -> Unit = {},
	) : TagDetailScaffoldUiState()

	public data class Detail(
		val isUpdateFinish: Boolean = false,
		val isDeleteFinish: Boolean = false,
		override val isTitleBlankError: Boolean = false,
		override val isNetworkError: Boolean = false,
		override val isUnknownError: Boolean = false,
		val update: (TagDetail) -> Unit = {},
		override val clearState: () -> Unit = {},
	) : TagDetailScaffoldUiState()
}
