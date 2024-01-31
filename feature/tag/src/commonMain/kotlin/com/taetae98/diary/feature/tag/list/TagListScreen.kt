package com.taetae98.diary.feature.tag.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    onTagClick: (String) -> Unit,
    tagLazyPagingItems: LazyPagingItems<TagUiState>
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = { TopBar() },
        floatingActionButton = { AddFloatingButton(onClick = onAdd) }
    ) {
        Content(
            modifier = Modifier.padding(it),
            onTagClick = onTagClick,
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
    onTagClick: (String) -> Unit,
    tagLazyPagingItems: LazyPagingItems<TagUiState>
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(
            count = tagLazyPagingItems.itemCount,
            key = tagLazyPagingItems.itemKey { it.id },
            contentType = tagLazyPagingItems.itemContentType { "tag" }
        ) {
            Tag(
                modifier = Modifier.fillParentMaxWidth(),
                uiState = tagLazyPagingItems[it],
                onTagClick = onTagClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Tag(
    modifier: Modifier = Modifier,
    uiState: TagUiState?,
    onTagClick: (String) -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = { uiState?.id?.let(onTagClick) }
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = uiState?.title.orEmpty()
        )
    }
}