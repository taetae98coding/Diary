package com.taetae98.diary.feature.tag.memo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems

@Composable
internal fun TagMemoRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    tagMemoViewModel: TagMemoViewModel,
) {
    TagMemoScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        memoLazyPagingItems = tagMemoViewModel.memoPagingData.collectAsLazyPagingItems(),
    )
}