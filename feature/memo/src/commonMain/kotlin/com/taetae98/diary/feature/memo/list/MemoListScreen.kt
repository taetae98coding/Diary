@file:OptIn(ExperimentalMaterial3Api::class)

package com.taetae98.diary.feature.memo.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.icon.TagIcon
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState
import com.taetae98.diary.ui.memo.compose.column.SwipeMemoColum

@Composable
internal fun MemoListScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    onTag: () -> Unit,
    hasTag: State<Boolean>,
    messageUiState: State<MemoListMessageUiState>,
    memoItems: LazyPagingItems<SwipeMemoUiState>,
    onMemo: (memoId: String) -> Unit,
) {
    val hostState = remember { SnackbarHostState() }

    DiaryScaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                hasTag = hasTag,
                onTag = onTag,
            )
        },
        snackbarHost = { SnackbarHost(hostState = hostState) },
        floatingActionButton = { AddFloatingButton(onClick = onAdd) },
    ) {
        SwipeMemoColum(
            modifier = Modifier.padding(it)
                .fillMaxSize(),
            memoItems = memoItems,
            onMemo = onMemo,
        )
    }

    Message(
        hostState = hostState,
        uiState = messageUiState,
    )
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    hasTag: State<Boolean>,
    onTag: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "메모") },
        actions = {
            IconButton(
                onClick = onTag,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = if (hasTag.value) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        LocalContentColor.current
                    },
                ),
            ) {
                TagIcon()
            }
        },
    )
}

@Composable
private fun Message(
    hostState: SnackbarHostState,
    uiState: State<MemoListMessageUiState>,
) {
    LaunchedEffect(uiState.value) {
        when (val message = uiState.value.message) {
            is MemoListMessage.Finish -> {
                if (hostState.showSnackbar(message = "완료", actionLabel = "취소") == SnackbarResult.ActionPerformed) {
                    message.cancel()
                }

                uiState.value.messageShow()
            }

            is MemoListMessage.Delete -> {
                if (hostState.showSnackbar(message = "삭제", actionLabel = "취소") == SnackbarResult.ActionPerformed) {
                    message.cancel()
                }
                uiState.value.messageShow()
            }

            else -> Unit
        }
    }
}
