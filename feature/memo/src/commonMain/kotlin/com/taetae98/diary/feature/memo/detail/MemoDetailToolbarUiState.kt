package com.taetae98.diary.feature.memo.detail

internal sealed class MemoDetailToolbarUiState {
    data object Add : MemoDetailToolbarUiState()

    data class Detail(
        val isComplete: Boolean,
        val onComplete: () -> Unit,
        val onDelete: () -> Unit,
    ) : MemoDetailToolbarUiState()
}