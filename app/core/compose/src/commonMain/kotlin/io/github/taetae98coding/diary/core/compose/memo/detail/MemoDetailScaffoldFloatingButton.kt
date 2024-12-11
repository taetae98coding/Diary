package io.github.taetae98coding.diary.core.compose.memo.detail

public sealed class MemoDetailScaffoldFloatingButton {
	public data object None : MemoDetailScaffoldFloatingButton()

	public data class Add(
		val isInProgress: Boolean,
		val add: () -> Unit,
	) : MemoDetailScaffoldFloatingButton()
}
