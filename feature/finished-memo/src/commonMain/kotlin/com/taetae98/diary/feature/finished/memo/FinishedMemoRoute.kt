package com.taetae98.diary.feature.finished.memo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems

@Composable
@NonRestartableComposable
internal fun FinishedMemoRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onMemo: (String) -> Unit,
    viewModel: FinishedMemoViewModel,
) {
    FinishedMemoScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        onMemo = onMemo,
        lazyPagingItems = viewModel.memoPaging.collectAsLazyPagingItems()
    )
}
