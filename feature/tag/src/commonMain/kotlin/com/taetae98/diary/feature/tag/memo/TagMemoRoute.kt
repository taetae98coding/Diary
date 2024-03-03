package com.taetae98.diary.feature.tag.memo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
internal fun TagMemoRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onNavigateToTagDetail: (String) -> Unit,
    onNavigateToMemoDetail: (String) -> Unit,
    tagMemoViewModel: TagMemoViewModel,
) {
    val tagId = tagMemoViewModel.tagId.collectAsStateOnLifecycle()

    TagMemoScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        onEdit = { tagId.value?.let(onNavigateToTagDetail) },
        onMemo = onNavigateToMemoDetail,
        memoItems = tagMemoViewModel.memoPagingData.collectAsLazyPagingItems(),
    )
}