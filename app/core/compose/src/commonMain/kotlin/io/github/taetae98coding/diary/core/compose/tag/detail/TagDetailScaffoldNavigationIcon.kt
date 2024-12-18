package io.github.taetae98coding.diary.core.compose.tag.detail

public sealed class TagDetailScaffoldNavigationIcon {
	public data object None : TagDetailScaffoldNavigationIcon()

	public data class NavigateUp(
		val navigateUp: () -> Unit,
	) : TagDetailScaffoldNavigationIcon()
}
