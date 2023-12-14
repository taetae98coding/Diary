package com.taetae98.diary.feature.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
internal fun AccountRoute(
    modifier: Modifier,
    onNavigateUp: () -> Unit,
    viewModel: AccountViewModel,
) {
    AccountScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        uiState = viewModel.uiState.collectAsState()
    )
}