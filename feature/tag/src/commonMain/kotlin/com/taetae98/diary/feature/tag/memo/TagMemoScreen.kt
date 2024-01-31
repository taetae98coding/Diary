package com.taetae98.diary.feature.tag.memo

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar

@Composable
internal fun TagMemoScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    memoLazyPagingItems: LazyPagingItems<Memo>
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
            memoLazyPagingItems = memoLazyPagingItems,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    memoLazyPagingItems: LazyPagingItems<Memo>,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            count = memoLazyPagingItems.itemCount,
            key = memoLazyPagingItems.itemKey { it.id },
            contentType = memoLazyPagingItems.itemContentType { "memo" },
        ) {
            Text(memoLazyPagingItems[it]?.title.orEmpty())
        }
    }
}
