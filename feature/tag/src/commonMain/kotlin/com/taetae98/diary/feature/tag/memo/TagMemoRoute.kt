package com.taetae98.diary.feature.tag.memo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
internal fun TagMemoRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onNavigateToTagDetail: () -> Unit,
    onNavigateToMemoAdd: () -> Unit,
    onNavigateToMemoDetail: (String) -> Unit,
    tagMemoViewModel: TagMemoViewModel,
    tagMemoPagingViewModel: TagMemoPagingViewModel,
) {
    TagMemoScreen(
        modifier = modifier,
        title = tagMemoViewModel.title.collectAsStateOnLifecycle(),
        message = tagMemoViewModel.message.collectAsStateOnLifecycle(),
        onNavigateUp = onNavigateUp,
        onEdit = onNavigateToTagDetail,
        onAdd = onNavigateToMemoAdd,
        onMemo = onNavigateToMemoDetail,
        memoItems = tagMemoPagingViewModel.memoPagingData.collectAsLazyPagingItems(),
    )
}