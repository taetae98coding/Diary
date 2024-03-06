package com.taetae98.diary.feature.memo.tag

internal data class NoTagMemoUiState(
    val isVisible: Boolean,
    val isSelected: Boolean,
    private val setHasToPage: (Boolean) -> Unit,
) {
    fun onClick() {
        setHasToPage(!isSelected)
    }
}