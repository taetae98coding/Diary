package com.taetae98.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
internal fun MemoDetailRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    memoDetailViewModel: MemoDetailViewModel,
    memoDetailTagViewModel: MemoDetailTagViewModel,
) {
    val uiState = memoDetailViewModel.uiState.collectAsStateOnLifecycle()

    MemoDetailScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        uiState = uiState,
        toolbarUiState = memoDetailViewModel.toolbarUiState.collectAsStateOnLifecycle(),
        titleUiState = memoDetailViewModel.titleUiStateHolder.uiState.collectAsStateOnLifecycle(),
        descriptionUiState = memoDetailViewModel.descriptionUiStateHolder.uiState.collectAsStateOnLifecycle(),
        dateRangeUiState = memoDetailViewModel.dateRangeUiStateHolder.uiState.collectAsStateOnLifecycle(),
        tagUiState = memoDetailTagViewModel.tagUiState.collectAsLazyPagingItems(),
    )

    LaunchedEffect(uiState.value.message) {
        when (uiState.value.message) {
            MemoDetailMessage.Update, MemoDetailMessage.Delete -> onNavigateUp()
            else -> Unit
        }
    }
}
