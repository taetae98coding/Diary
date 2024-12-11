package io.github.taetae98coding.diary.core.compose.memo.tag

public sealed class MemoTagNavigationIcon {
	public data object None : MemoTagNavigationIcon()

	public data class NavigateUp(
		val navigateUp: () -> Unit,
	) : MemoTagNavigationIcon()
}
