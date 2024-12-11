package io.github.taetae98coding.diary.core.compose.memo.detail

public sealed class MemoDetailScaffoldActions {
	public data object None : MemoDetailScaffoldActions()

	public data class FinishAndDelete(
		val isFinish: Boolean = false,
		val finish: () -> Unit = {},
		val restart: () -> Unit = {},
		val delete: () -> Unit = {},
	) : MemoDetailScaffoldActions()
}
