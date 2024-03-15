package com.taetae98.diary.feature.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
@NonRestartableComposable
internal fun AccountRoute(
    modifier: Modifier,
    onNavigateUp: () -> Unit,
    viewModel: AccountViewModel,
) {
    AccountScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        uiState = viewModel.uiState.collectAsStateOnLifecycle()
    )
}