package io.github.taetae98coding.diary.core.compose.memo.detail

public sealed class MemoDetailScaffoldNavigationIcon {
	public data object None : MemoDetailScaffoldNavigationIcon()

	public data class NavigateUp(
		val navigateUp: () -> Unit,
	) : MemoDetailScaffoldNavigationIcon()
}
