package com.taetae98.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
internal fun MemoDetailRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    memoDetailViewModel: MemoDetailViewModel,
) {
    MemoDetailScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        uiState = memoDetailViewModel.uiState.collectAsStateOnLifecycle(),
        toolbarUiState = memoDetailViewModel.toolbarUiState.collectAsStateOnLifecycle(),
        titleUiState = memoDetailViewModel.titleUiState.collectAsStateOnLifecycle(),
    )
}