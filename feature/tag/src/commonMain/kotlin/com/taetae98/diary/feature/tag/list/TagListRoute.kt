package com.taetae98.diary.feature.tag.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems

@Composable
internal fun TagListRoute(
    modifier: Modifier = Modifier,
    onNavigateToTagAdd: () -> Unit,
    viewModel: TagListViewModel,
) {
    TagListScreen(
        modifier = modifier,
        onAdd = onNavigateToTagAdd,
        tagLazyPagingItems = viewModel.tagPagingData.collectAsLazyPagingItems(),
    )
}