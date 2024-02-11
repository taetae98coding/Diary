package com.taetae98.diary.feature.memo.tag

internal data class TagUiState(
    val id: String,
    val isSelected: Boolean,
    val title: String,
    private val onClick: (id: String) -> Unit,
) {
    fun onClick() {
        onClick(id)
    }
}
