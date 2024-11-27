package io.github.taetae98coding.diary.feature.tag.list

internal sealed class TagListFloatingButton {
	data object None : TagListFloatingButton()

	data class Add(
		val onAdd: () -> Unit,
	) : TagListFloatingButton()
}
