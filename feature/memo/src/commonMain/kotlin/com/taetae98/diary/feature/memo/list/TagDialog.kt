@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.taetae98.diary.feature.memo.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.taetae98.diary.feature.memo.tag.TagUiState
import com.taetae98.diary.ui.compose.icon.ClearIcon
import com.taetae98.diary.ui.compose.icon.TagIcon
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
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        items = selectTagList.value,
                        key = { it.id },
                        contentType = { "Tag" }
                    ) { uiState ->
                        InputChip(
                            modifier = Modifier.animateItemPlacement(),
                            selected = false,
                            onClick = { uiState.onClick() },
                            label = { Text(text = uiState.title) },
                            leadingIcon = { TagIcon() },
                            trailingIcon = { ClearIcon() }
                        )
                    }
                }
                Text(
                    text = "태그",
                    style = MaterialTheme.typography.titleLarge,
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        count = nonSelectTagLazyPagingItems.itemCount,
                        key = nonSelectTagLazyPagingItems.itemKey { it.id },
                        contentType = { "Tag" },
                    ) {
                        val uiState = nonSelectTagLazyPagingItems[it]

                        InputChip(
                            modifier = Modifier.animateItemPlacement(),
                            selected = false,
                            onClick = { uiState?.onClick() },
                            label = { Text(text = uiState?.title.orEmpty()) },
                            leadingIcon = { TagIcon() },
                        )
                    }
                }
            }
        }
    }
}
