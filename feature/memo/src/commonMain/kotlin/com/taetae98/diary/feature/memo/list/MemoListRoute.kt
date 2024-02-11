package com.taetae98.diary.feature.memo.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems

@Composable
internal fun MemoListRoute(
    modifier: Modifier = Modifier,
    onNavigateToMemoAdd: () -> Unit,
    viewModel: MemoListViewModel,
    onNavigateToMemoDetail: (memoId: String) -> Unit,
) {
    MemoListScreen(
        modifier = modifier,
        onAdd = onNavigateToMemoAdd,
        memoItems = viewModel.memoPagingData.collectAsLazyPagingItems(),
        onNavigateToMemoDetail = onNavigateToMemoDetail,
    )
}