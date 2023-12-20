package com.taetae98.diary.ui.compose.entity

public data class EntityDetailUiState(
    val title: String,
    val setTitle: (String) -> Unit,
) {
}