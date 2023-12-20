package com.taetae98.diary.feature.memo.detail

internal sealed class MemoDetailUiState {
    data class Add(val onAdd: () -> Unit) : MemoDetailUiState()
}