package io.github.taetae98coding.diary.feature.memo.detail

internal sealed interface MemoDetailEffect {
    data object None : MemoDetailEffect
    data object UpdateFinish : MemoDetailEffect
    data object CopyFinish : MemoDetailEffect
    data object DeleteFinish : MemoDetailEffect
    data object UnknownError : MemoDetailEffect
}
