@file:OptIn(ExperimentalFoundationApi::class)

package com.taetae98.diary.feature.finished.memo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.memo.compose.Memo
import com.taetae98.diary.ui.memo.compose.MemoColum
import com.taetae98.diary.ui.memo.compose.MemoUiState

@Composable
internal fun FinishedMemoScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onMemo: (String) -> Unit,
    lazyPagingItems: LazyPagingItems<MemoUiState>,
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = {
            NavigateUpTopBar(onNavigateUp = onNavigateUp)
        },
    ) {
        Content(
            modifier = Modifier.padding(it),
            onMemo = onMemo,
            lazyPagingItems = lazyPagingItems,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    onMemo: (String) -> Unit,
    lazyPagingItems: LazyPagingItems<MemoUiState>,
) {
    MemoColum(modifier = modifier) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id },
            contentType = lazyPagingItems.itemContentType { "Memo" },
        ) {
            val uiState = lazyPagingItems[it]

            Memo(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .animateItemPlacement()
                    .clickable(
                        enabled = uiState != null,
                        onClickLabel = uiState?.title,
                        onClick = { uiState?.id?.let(onMemo) },
                    ),
                uiState = uiState,
            )
        }
    }
}
