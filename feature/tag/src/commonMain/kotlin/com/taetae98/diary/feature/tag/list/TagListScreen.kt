package com.taetae98.diary.feature.tag.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.topbar.TitleTopBar

@Composable
internal fun TagListScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    tagLazyPagingItems: LazyPagingItems<String>
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = { TopBar() },
        floatingActionButton = { AddFloatingButton(onClick = onAdd) }
    ) {
        Content(
            modifier = Modifier.padding(it),
            tagLazyPagingItems = tagLazyPagingItems,
        )
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier
) {
    TitleTopBar(
        modifier = modifier,
        title = "태그"
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    tagLazyPagingItems: LazyPagingItems<String>
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            count = tagLazyPagingItems.itemCount,
            key = tagLazyPagingItems.itemKey { it },
            contentType = tagLazyPagingItems.itemContentType { "tag" }
        ) {
            Text(text = tagLazyPagingItems[it].orEmpty())
        }
    }
}