package com.taetae98.diary.feature.tag.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems

@Composable
@NonRestartableComposable
internal fun TagListRoute(
    modifier: Modifier = Modifier,
    onNavigateToTagAdd: () -> Unit,
    onNavigateToTagMemo: (String) -> Unit,
    viewModel: TagListViewModel,
) {
    TagListScreen(
        modifier = modifier,
        onAdd = onNavigateToTagAdd,
        onTagClick = onNavigateToTagMemo,
        tagLazyPagingItems = viewModel.tagPagingData.collectAsLazyPagingItems(),
    )
}