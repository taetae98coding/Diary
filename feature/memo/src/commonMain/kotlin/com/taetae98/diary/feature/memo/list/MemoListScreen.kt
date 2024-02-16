package com.taetae98.diary.feature.memo.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.topbar.TitleTopBar
import com.taetae98.diary.ui.memo.compose.SwipeMemo
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState

@Composable
internal fun MemoListScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    memoItems: LazyPagingItems<SwipeMemoUiState>,
    onNavigateToMemoDetail: (memoId: String) -> Unit,
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = { TitleTopBar(title = "메모") },
        floatingActionButton = { AddFloatingButton(onClick = onAdd) }
    ) {
        Content(
            modifier = Modifier.padding(it)
                .fillMaxSize(),
            memoItems = memoItems,
            onNavigateToMemoDetail = onNavigateToMemoDetail,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    memoItems: LazyPagingItems<SwipeMemoUiState>,
    onNavigateToMemoDetail: (memoId: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(
            count = memoItems.itemCount,
            key = memoItems.itemKey { it.memo.id },
            contentType = { "Memo" },
        ) {
            val uiState = memoItems[it]

            SwipeMemo(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .animateItemPlacement()
                    .clickable(
                        enabled = uiState != null,
                        onClickLabel = uiState?.memo?.title,
                        onClick = { uiState?.memo?.id?.let(onNavigateToMemoDetail) }
                    ),
                uiState = uiState,
            )
        }
    }
}
