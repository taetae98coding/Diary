package io.github.taetae98coding.diary.feature.memo.detail

internal sealed class MemoDetailNavigationButton {
	data object None : MemoDetailNavigationButton()

	data class NavigateUp(
		val onNavigateUp: () -> Unit,
	) : MemoDetailNavigationButton()
}
