package io.github.taetae98coding.diary.feature.tag.add

internal sealed class TagAddEffect {
    data object None : TagAddEffect()

    data object AddFinish : TagAddEffect()

    data object TitleBlank : TagAddEffect()

    data object UnknownError : TagAddEffect()
}
