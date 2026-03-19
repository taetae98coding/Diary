package io.github.taetae98coding.diary.presenter.memo.api

public sealed class MemoDetailEffect {
    public data object None : MemoDetailEffect()

    public data object UpdateFinish : MemoDetailEffect()

    public data object DeleteFinish : MemoDetailEffect()

    public data object UnknownError : MemoDetailEffect()
}
