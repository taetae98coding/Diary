package com.taetae98.diary.feature.memo.detail

internal sealed class MemoDetailUiState {
    abstract val message: MemoDetailMessage?
    abstract val onMessageShown: () -> Unit

    data class Add(
        val onAdd: () -> Unit,
        override val message: MemoDetailMessage?,
        override val onMessageShown: () -> Unit,
    ) : MemoDetailUiState()

    data class Detail(
        override val message: MemoDetailMessage?,
        override val onMessageShown: () -> Unit,
    ) : MemoDetailUiState()
}