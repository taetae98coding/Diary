package com.taetae98.diary.feature.finished.memo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.memo.compose.Memo
import com.taetae98.diary.ui.memo.compose.MemoUiState

@Composable
internal fun FinishedMemoScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    lazyPagingItems: LazyPagingItems<MemoUiState>,
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = {
            NavigateUpTopBar(onNavigateUp = onNavigateUp)
        }
    ) {
        Content(
            modifier = Modifier.padding(it),
            lazyPagingItems = lazyPagingItems,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<MemoUiState>,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id },
            contentType = lazyPagingItems.itemContentType { "Memo" }
        ) {
            Memo(
                modifier = Modifier.fillMaxWidth(),
                uiState = lazyPagingItems[it],
            )
        }
    }
}
