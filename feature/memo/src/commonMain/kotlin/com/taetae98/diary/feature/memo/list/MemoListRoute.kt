package com.taetae98.diary.feature.memo.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
internal fun MemoListRoute(
    modifier: Modifier = Modifier,
    onNavigateToMemoAdd: () -> Unit,
    onNavigateToMemoTag: () -> Unit,
    viewModel: MemoListViewModel,
    tagViewModel: MemoListTagViewModel,
    onNavigateToMemoDetail: (memoId: String) -> Unit,
) {
    MemoListScreen(
        modifier = modifier,
        onAdd = onNavigateToMemoAdd,
        onTag = onNavigateToMemoTag,
        hasTag = tagViewModel.hasTag.collectAsStateOnLifecycle(),
        messageUiState = viewModel.messageUiState.collectAsStateOnLifecycle(),
        memoItems = viewModel.memoPagingData.collectAsLazyPagingItems(),
        onMemo = onNavigateToMemoDetail,
    )
}