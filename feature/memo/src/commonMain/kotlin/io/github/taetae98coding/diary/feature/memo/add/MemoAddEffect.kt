package io.github.taetae98coding.diary.feature.memo.add

internal sealed class MemoAddEffect {
    data object None : MemoAddEffect()
    data object AddFinish : MemoAddEffect()
    data object TitleBlank : MemoAddEffect()
    data object UnknownError : MemoAddEffect()
}
