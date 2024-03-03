@file:OptIn(ExperimentalFoundationApi::class)

package com.taetae98.diary.ui.memo.compose.column

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.taetae98.diary.ui.memo.compose.SwipeMemo
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState

@Composable
public fun SwipeMemoColum(
    modifier: Modifier = Modifier,
    memoItems: LazyPagingItems<SwipeMemoUiState>,
    onMemo: (memoId: String) -> Unit,
) {
    MemoColum(modifier = modifier) {
        items(
            count = memoItems.itemCount,
            key = memoItems.itemKey { it.memo.id },
            contentType = { "Memo" },
        ) {
            val uiState = memoItems[it]

            SwipeMemo(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .animateItemPlacement()
                    .clickable(
                        enabled = uiState != null,
                        onClickLabel = uiState?.memo?.title,
                        onClick = { uiState?.memo?.id?.let(onMemo) },
                    ),
                uiState = uiState,
            )
        }
    }
}
