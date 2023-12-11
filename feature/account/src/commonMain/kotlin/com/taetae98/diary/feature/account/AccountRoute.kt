package com.taetae98.diary.feature.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
internal fun AccountRoute(
    modifier: Modifier,
    onNavigateUp: () -> Unit,
) {
    AccountScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        uiState = remember { mutableStateOf(AccountUiState.Guest) }
    )
}