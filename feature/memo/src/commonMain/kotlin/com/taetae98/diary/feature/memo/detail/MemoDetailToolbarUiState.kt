package com.taetae98.diary.feature.memo.detail

internal sealed class MemoDetailToolbarUiState {
    data object Add : MemoDetailToolbarUiState()

    data class Detail(
        val isFinished: Boolean,
        val onFinish: () -> Unit,
        val onDelete: () -> Unit,
    ) : MemoDetailToolbarUiState()
}