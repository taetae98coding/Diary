@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)

package com.taetae98.diary.feature.memo.tag

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.taetae98.diary.ui.compose.icon.ClearIcon
import com.taetae98.diary.ui.compose.icon.TagIcon
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun TagDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    selectTagList: State<ImmutableList<TagUiState>>,
    nonSelectTagLazyPagingItems: LazyPagingItems<TagUiState>,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "확인")
            }
        },
        text = {
            Column {
                SelectLayout(tagList = selectTagList)
                TagLayout(pagingItems = nonSelectTagLazyPagingItems)
                EtcLayout()
            }
        },
    )
}

@Composable
private fun SelectLayout(
    modifier: Modifier = Modifier,
    tagList: State<ImmutableList<TagUiState>>,
) {
    Column(modifier = modifier) {
        Text(
            text = "선택된 태그",
            style = MaterialTheme.typography.titleLarge,
        )

        if (tagList.value.isEmpty()) {
            EmptyLayout(modifier = Modifier.fillMaxWidth())
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(
                    items = tagList.value,
                    key = { it.id },
                    contentType = { "Tag" },
                ) { uiState ->
                    InputChip(
                        modifier = Modifier.animateItemPlacement(),
                        selected = false,
                        onClick = { uiState.onClick() },
                        label = { Text(text = uiState.title) },
                        leadingIcon = { TagIcon() },
                        trailingIcon = { ClearIcon() },
                    )
                }
            }
        }
    }
}

@Composable
private fun TagLayout(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<TagUiState>,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = "태그",
            style = MaterialTheme.typography.titleLarge,
        )

        if (pagingItems.itemCount == 0) {
            EmptyLayout(modifier = Modifier.fillMaxWidth())
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id },
                    contentType = { "Tag" },
                ) {
                    val uiState = pagingItems[it]

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

@Composable
private fun EtcLayout() {
    Text(
        text = "옵션",
        style = MaterialTheme.typography.titleLarge,
    )

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        AssistChip(
            onClick = {},
            label = { Text("태그가 없는 메모") },
        )
    }
}

@Composable
private fun EmptyLayout(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .heightIn(min = 50.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "태그가 없습니다.")
    }
}