package com.taetae98.diary.feature.tag.memo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.memo.compose.SwipeMemo
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState

@Composable
internal fun TagMemoScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    memoItems: LazyPagingItems<SwipeMemoUiState>
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = {
            NavigateUpTopBar(
                onNavigateUp = onNavigateUp,
            )
        }
    ) {
        Content(
            modifier = Modifier.padding(it),
            memoItems = memoItems,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    memoItems: LazyPagingItems<SwipeMemoUiState>,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            count = memoItems.itemCount,
            key = memoItems.itemKey { it.memo.id },
            contentType = memoItems.itemContentType { "memo" },
        ) {
            val uiState = memoItems[it]

            SwipeMemo(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .animateItemPlacement(),
                uiState = uiState,
            )
        }
    }
}
