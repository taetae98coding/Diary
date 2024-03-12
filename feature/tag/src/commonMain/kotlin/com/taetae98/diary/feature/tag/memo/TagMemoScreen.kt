@file:OptIn(ExperimentalMaterial3Api::class)

package com.taetae98.diary.feature.tag.memo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.icon.EditIcon
import com.taetae98.diary.ui.compose.icon.NavigateUpIcon
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState
import com.taetae98.diary.ui.memo.compose.column.SwipeMemoColum

@Composable
internal fun TagMemoScreen(
    modifier: Modifier = Modifier,
    title: State<String>,
    message: State<TagMemoMessage?>,
    onNavigateUp: () -> Unit,
    onEdit: () -> Unit,
    onAdd: () -> Unit,
    onMemo: (memoId: String) -> Unit,
    memoItems: LazyPagingItems<SwipeMemoUiState>,
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                title = title,
                onNavigateUp = onNavigateUp,
                onEdit = onEdit,
            )
        },
        floatingActionButton = {
            AddFloatingButton(onClick = onAdd)
        },
    ) {
        SwipeMemoColum(
            modifier = Modifier.padding(it)
                .fillMaxSize(),
            memoItems = memoItems,
            onMemo = onMemo,
        )
    }

    Message(
        onNavigateUp = onNavigateUp,
        state = message,
    )
}

@Composable
private fun Message(
    onNavigateUp: () -> Unit,
    state: State<TagMemoMessage?>,
) {
    LaunchedEffect(state.value) {
        when (val message = state.value) {
            is TagMemoMessage.NotFound -> {
                onNavigateUp()
                message.onMessageShown()
            }

            else -> Unit
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    title: State<String>,
    onNavigateUp: () -> Unit,
    onEdit: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = title.value)
        },
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                NavigateUpIcon()
            }
        },
        actions = {
            IconButton(onClick = onEdit) {
                EditIcon()
            }
        },
    )
}