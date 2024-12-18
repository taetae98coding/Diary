package io.github.taetae98coding.diary.core.compose.tag.memo

public sealed class TagMemoNavigateIcon {
	public data object None : TagMemoNavigateIcon()

	public data class NavigateUp(
		val navigateUp: () -> Unit,
	) : TagMemoNavigateIcon()
}
