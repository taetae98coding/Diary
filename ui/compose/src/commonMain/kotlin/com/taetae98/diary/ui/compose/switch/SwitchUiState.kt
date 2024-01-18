package com.taetae98.diary.ui.compose.switch

public data class SwitchUiState(
    val value: Boolean,
    val onValueChange: (Boolean) -> Unit,
)
