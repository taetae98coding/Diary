package com.taetae98.diary.feature.memo.list

import androidx.compose.runtime.Composable
import app.cash.paging.compose.collectAsLazyPagingItems
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
internal fun TagDialogRoute(
    onDismiss: () -> Unit,
    tagViewModel: TagViewModel,
) {
    TagDialog(
        onDismiss = onDismiss,
        selectTagList = tagViewModel.selectTagList.collectAsStateOnLifecycle(),
        nonSelectTagLazyPagingItems = tagViewModel.notSelectTagPagingData.collectAsLazyPagingItems(),
    )
}
