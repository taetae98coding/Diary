package com.taetae98.diary.feature.memo.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.taetae98.diary.domain.entity.account.Memo
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.topbar.TitleTopBar

@Composable
internal fun MemoListScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    memoItems: LazyPagingItems<Memo>,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TitleTopBar(title = "메모") },
        floatingActionButton = { AddFloatingButton(onAdd = onAdd) }
    ) {
        Content(
            modifier = Modifier.padding(it),
            memoItems = memoItems,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    memoItems: LazyPagingItems<Memo>,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            count = memoItems.itemCount,
            key = memoItems.itemKey { it.id },
            contentType = { "Memo" },
        ) {
            Text(text = memoItems[it]?.title.orEmpty())
        }
    }
}