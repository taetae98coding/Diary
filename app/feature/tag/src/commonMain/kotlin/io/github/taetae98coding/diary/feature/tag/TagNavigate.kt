package io.github.taetae98coding.diary.feature.tag

internal sealed class TagNavigate {
    data object None : TagNavigate()

    data object Add : TagNavigate()

    data class Tag(val tagId: String) : TagNavigate()
}
