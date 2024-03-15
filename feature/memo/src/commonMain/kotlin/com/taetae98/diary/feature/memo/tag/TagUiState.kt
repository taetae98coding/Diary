package com.taetae98.diary.feature.memo.tag

internal data class TagUiState(
    val id: String,
    val isSelected: Boolean,
    val title: String,
    private val select: (id: String) -> Unit,
    private val unselect: (id: String) -> Unit,
) {
    fun onClick() {
        if (isSelected) {
            unselect(id)
        } else {
            select(id)
        }
    }
}
