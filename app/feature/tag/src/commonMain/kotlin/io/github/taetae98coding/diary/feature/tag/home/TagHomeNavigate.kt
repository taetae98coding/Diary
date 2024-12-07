package io.github.taetae98coding.diary.feature.tag.home

internal sealed class TagHomeNavigate {
	data object None : TagHomeNavigate()

	data object Add : TagHomeNavigate()

	data class Tag(
		val tagId: String,
	) : TagHomeNavigate()
}
