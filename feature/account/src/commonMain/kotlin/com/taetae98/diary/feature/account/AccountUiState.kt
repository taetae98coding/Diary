package com.taetae98.diary.feature.account

import androidx.compose.runtime.Stable

@Stable
internal sealed class AccountUiState {
    data object Guest : AccountUiState()

    data object Member : AccountUiState()
}