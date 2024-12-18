package io.github.taetae98coding.diary.core.compose.tag.detail

public sealed class TagDetailScaffoldFloatingButton {
	public data object None : TagDetailScaffoldFloatingButton()

	public data class Add(
		val isInProgress: Boolean,
		val add: () -> Unit,
	) : TagDetailScaffoldFloatingButton()
}
