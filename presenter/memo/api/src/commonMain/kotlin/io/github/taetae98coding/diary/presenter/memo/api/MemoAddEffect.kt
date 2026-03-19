package io.github.taetae98coding.diary.presenter.memo.api

public sealed class MemoAddEffect {
    public data object None : MemoAddEffect()

    public data object AddFinish : MemoAddEffect()

    public data object TitleBlank : MemoAddEffect()

    public data object UnknownError : MemoAddEffect()
}
