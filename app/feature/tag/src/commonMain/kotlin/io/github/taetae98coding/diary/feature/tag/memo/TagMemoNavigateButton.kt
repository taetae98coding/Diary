package io.github.taetae98coding.diary.feature.tag.memo

internal sealed class TagMemoNavigateButton {
	data object None : TagMemoNavigateButton()

	data class NavigateUp(
		val onNavigateUp: () -> Unit,
	) : TagMemoNavigateButton()
}
