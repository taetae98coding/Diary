package io.github.taetae98coding.diary.feature.tag.detail

internal sealed class TagDetailNavigationButton {
	data object None : TagDetailNavigationButton()

	data class NavigateUp(val onNavigateUp: () -> Unit) : TagDetailNavigationButton()
}
