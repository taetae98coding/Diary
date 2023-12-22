package com.taetae98.diary.feature.memo.list

internal data class MemoListUiState(
    val id: String,
    val title: String,
    private val delete: (String) -> Unit,
) {
    fun onDelete() {
        delete(id)
    }
}