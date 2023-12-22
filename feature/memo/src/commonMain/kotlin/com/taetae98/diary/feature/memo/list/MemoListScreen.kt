package com.taetae98.diary.feature.memo.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.topbar.TitleTopBar

@Composable
internal fun MemoListScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    memoItems: LazyPagingItems<MemoListUiState>,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TitleTopBar(title = "메모") },
        floatingActionButton = { AddFloatingButton(onAdd = onAdd) }
    ) {
        Content(
            modifier = Modifier.padding(it)
                .fillMaxSize(),
            memoItems = memoItems,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    memoItems: LazyPagingItems<MemoListUiState>,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(
            count = memoItems.itemCount,
            key = memoItems.itemKey { it.id },
            contentType = { "Memo" },
        ) {
            Memo(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
                uiState = memoItems[it]
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Memo(
    modifier: Modifier = Modifier,
    uiState: MemoListUiState?
) {
    Card(
        modifier = modifier,
        onClick = {
            uiState?.onDelete()
        }
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = uiState?.title.orEmpty()
        )
    }
}