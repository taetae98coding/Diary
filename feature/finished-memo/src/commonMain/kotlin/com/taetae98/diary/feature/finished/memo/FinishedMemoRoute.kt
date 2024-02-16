package com.taetae98.diary.feature.finished.memo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems

@Composable
internal fun FinishedMemoRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    viewModel: FinishedMemoViewModel,
) {
    FinishedMemoScreen(
        modifier=modifier,
        onNavigateUp = onNavigateUp,
        lazyPagingItems = viewModel.memoPaging.collectAsLazyPagingItems()
    )
}
