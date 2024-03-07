package com.taetae98.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
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
        toolbarUiState = memoDetailToolbarViewModel.uiState.collectAsStateOnLifecycle(),
        titleUiState = memoDetailViewModel.titleUiStateHolder.uiState.collectAsStateOnLifecycle(),
        descriptionUiState = memoDetailViewModel.descriptionUiStateHolder.uiState.collectAsStateOnLifecycle(),
        dateRangeUiState = memoDetailViewModel.dateRangeUiStateHolder.uiState.collectAsStateOnLifecycle(),
        tagUiState = memoDetailTagViewModel.tagUiState.collectAsLazyPagingItems(),
    )
}
