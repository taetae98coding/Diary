package io.github.taetae98coding.diary.core.compose.tag.list

public sealed class TagListScaffoldFloatingButton {
	public data object None : TagListScaffoldFloatingButton()

	public data class Add(
		public val add: () -> Unit,
	) : TagListScaffoldFloatingButton()
}
