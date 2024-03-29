package com.taetae98.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
@NonRestartableComposable
internal fun MemoDetailRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    memoDetailViewModel: MemoDetailViewModel,
    memoDetailToolbarViewModel: MemoDetailToolbarViewModel,
    memoDetailTagViewModel: MemoDetailTagViewModel,
) {
    MemoDetailScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        uiState = memoDetailViewModel.uiState,
        message = memoDetailViewModel.message.collectAsStateOnLifecycle(),
        toolbarMessage = memoDetailToolbarViewModel.message.collectAsStateOnLifecycle(),
        toolbarUiState = memoDetailToolbarViewModel.uiState.collectAsStateOnLifecycle(),
        titleUiState = memoDetailViewModel.titleUiStateHolder.uiState.collectAsStateOnLifecycle(),
        descriptionUiState = memoDetailViewModel.descriptionUiStateHolder.uiState.collectAsStateOnLifecycle(),
        dateRangeUiState = memoDetailViewModel.dateRangeUiStateHolder.uiState.collectAsStateOnLifecycle(),
        tagUiState = memoDetailTagViewModel.tagUiState.collectAsLazyPagingItems(),
    )
}
