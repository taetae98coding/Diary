package com.taetae98.diary.ui.memo.compose

public data class SwipeMemoUiState(
    val memo: MemoUiState,
    private val complete: (id: String) -> Unit,
    private val delete: (id: String) -> Unit,
) {
    public fun onComplete() {
        complete(memo.id)
    }

    public fun onDelete() {
        delete(memo.id)
    }
}