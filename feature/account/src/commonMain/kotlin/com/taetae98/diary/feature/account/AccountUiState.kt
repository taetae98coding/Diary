package com.taetae98.diary.feature.account

import androidx.compose.runtime.Stable
import com.taetae98.diary.domain.entity.account.Credential

@Stable
internal sealed class AccountUiState {
    data class Guest(
        val onSignIn: (Credential) -> Unit,
    ) : AccountUiState()

    data class Member(
        val uid: String,
        val email: String?,
        val onSignOut: () -> Unit,
    ) : AccountUiState()
}