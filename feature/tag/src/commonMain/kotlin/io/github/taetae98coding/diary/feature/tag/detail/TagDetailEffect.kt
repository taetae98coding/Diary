package io.github.taetae98coding.diary.feature.tag.detail

internal sealed class TagDetailEffect {
    data object None : TagDetailEffect()

    data object UpdateFinish : TagDetailEffect()

    data object DeleteFinish : TagDetailEffect()

    data object UnknownError : TagDetailEffect()
}
