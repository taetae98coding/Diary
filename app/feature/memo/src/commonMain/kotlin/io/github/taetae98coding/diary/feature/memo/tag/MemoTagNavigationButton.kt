package io.github.taetae98coding.diary.feature.memo.tag

internal sealed class MemoTagNavigationButton {
	data object None : MemoTagNavigationButton()

	data class NavigateUp(val onNavigateUp: () -> Unit) : MemoTagNavigationButton()
}
