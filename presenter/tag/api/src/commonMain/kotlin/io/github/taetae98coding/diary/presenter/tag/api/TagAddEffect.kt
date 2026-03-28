package io.github.taetae98coding.diary.presenter.tag.api

public sealed class TagAddEffect {
    public data object None : TagAddEffect()

    public data object AddFinish : TagAddEffect()

    public data object TitleBlank : TagAddEffect()

    public data object UnknownError : TagAddEffect()
}
