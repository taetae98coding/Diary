package com.taetae98.diary.ui.memo.compose

public data class SwipeMemoUiState(
    val memo: MemoUiState,
    private val finish: (id: String) -> Unit,
    private val delete: (id: String) -> Unit,
) {
    public fun onFinish() {
        finish(memo.id)
    }

    public fun onDelete() {
        delete(memo.id)
    }
}