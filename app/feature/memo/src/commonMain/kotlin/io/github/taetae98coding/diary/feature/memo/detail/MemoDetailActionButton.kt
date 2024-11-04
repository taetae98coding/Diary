package io.github.taetae98coding.diary.feature.memo.detail

internal sealed class MemoDetailActionButton {
    data object None : MemoDetailActionButton()
    data class FinishAndDetail(
        val isFinish: Boolean,
        val onFinishChange: (Boolean) -> Unit,
        val delete: () -> Unit,
    ) : MemoDetailActionButton()
}
