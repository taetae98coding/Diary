package io.github.taetae98coding.diary.core.compose.tag

internal sealed class TagListDetailNavigate {
	data object None : TagListDetailNavigate()

	data object Add : TagListDetailNavigate()

	data class Tag(
		val tagId: String,
	) : TagListDetailNavigate()
}
