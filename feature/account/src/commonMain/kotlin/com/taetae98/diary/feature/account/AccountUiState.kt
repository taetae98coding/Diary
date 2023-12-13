package com.taetae98.diary.feature.account

import androidx.compose.runtime.Stable

@Stable
internal sealed class AccountUiState {
    data class Guest(
        val onLogin: () -> Unit,
    ) : AccountUiState()

    data object Member : AccountUiState()
}