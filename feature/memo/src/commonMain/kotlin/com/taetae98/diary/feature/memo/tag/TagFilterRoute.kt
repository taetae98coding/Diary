package com.taetae98.diary.feature.memo.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import app.cash.paging.compose.collectAsLazyPagingItems
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
@NonRestartableComposable
internal fun TagDialogRoute(
    onDismiss: () -> Unit,
    tagFilterTagListViewModel: TagFilterTagListViewModel,
    tagFilterNoTagMemoViewModel: TagFilterNoTagMemoViewModel,
) {


    TagDialog(
        onDismiss = onDismiss,
        selectTagList = tagFilterTagListViewModel.selectTagList.collectAsStateOnLifecycle(),
        nonSelectTagLazyPagingItems = tagFilterTagListViewModel.notSelectTagPagingData.collectAsLazyPagingItems(),
        noTagMemoUiState = tagFilterNoTagMemoViewModel.uiState.collectAsStateOnLifecycle(),
    )
}
