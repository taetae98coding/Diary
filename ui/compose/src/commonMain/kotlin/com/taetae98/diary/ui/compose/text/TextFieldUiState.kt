package com.taetae98.diary.ui.compose.text

public data class TextFieldUiState(
    val value: String,
    val onValueChange: (String) -> Unit,
) {
}