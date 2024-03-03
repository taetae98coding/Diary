package com.taetae98.diary.feature.tag.memo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import com.taetae98.diary.ui.compose.icon.EditIcon
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState
import com.taetae98.diary.ui.memo.compose.column.SwipeMemoColum

@Composable
internal fun TagMemoScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onEdit: () -> Unit,
    onMemo: (memoId: String) -> Unit,
    memoItems: LazyPagingItems<SwipeMemoUiState>,
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = {
            NavigateUpTopBar(
                onNavigateUp = onNavigateUp,
                actions = {
                    IconButton(onClick = onEdit) {
                        EditIcon()
                    }
                },
            )
        },
    ) {
        SwipeMemoColum(
            modifier = Modifier.padding(it)
                .fillMaxSize(),
            memoItems = memoItems,
            onMemo = onMemo,
        )
    }
}
