package com.taetae98.diary.feature.memo.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.taetae98.diary.feature.memo.tag.TagUiState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun TagDialog(
    onDismiss: () -> Unit,
    selectTagList: State<ImmutableList<TagUiState>>,
    nonSelectTagLazyPagingItems: LazyPagingItems<TagUiState>,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                Text(
                    text = "선택된 태그",
                    style = MaterialTheme.typography.titleLarge,
                )
                LazyRow {
                    items(
                        items = selectTagList.value,
                        key = { it.id },
                        contentType = { "Tag" }
                    ) {
                        Text(it.title)
                    }
                }
                Text(
                    text = "태그",
                    style = MaterialTheme.typography.titleLarge,
                )
                LazyRow {
                    items(
                        count = nonSelectTagLazyPagingItems.itemCount,
                        key = nonSelectTagLazyPagingItems.itemKey { it.id },
                        contentType = { "Tag" },
                    ) {
                        Text(nonSelectTagLazyPagingItems[it]?.title.orEmpty())
                    }
                }
            }
        }
    }
}
