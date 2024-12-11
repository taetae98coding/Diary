package io.github.taetae98coding.diary.core.compose.memo.detail

import io.github.taetae98coding.diary.core.model.memo.MemoDetail

public sealed class MemoDetailScaffoldUiState {
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
		val add: (MemoDetail) -> Unit = {},
		override val clearState: () -> Unit,
	) : MemoDetailScaffoldUiState()

	public data class Detail(
		val isUpdateFinish: Boolean = false,
		val isUpdateFail: Boolean = false,
		val isDeleteFinish: Boolean = false,
		override val isTitleBlankError: Boolean = false,
		override val isNetworkError: Boolean = false,
		override val isUnknownError: Boolean = false,
		val update: (MemoDetail) -> Unit = {},
		override val clearState: () -> Unit,
	) : MemoDetailScaffoldUiState()
}
