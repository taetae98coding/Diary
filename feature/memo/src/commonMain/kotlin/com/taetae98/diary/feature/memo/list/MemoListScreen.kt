package com.taetae98.diary.feature.memo.list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.taetae98.diary.feature.memo.MemoDefaults
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.icon.CircleIcon
import com.taetae98.diary.ui.compose.icon.DeleteIcon
import com.taetae98.diary.ui.compose.icon.FinishIcon
import com.taetae98.diary.ui.compose.topbar.TitleTopBar

@Composable
internal fun MemoListScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    memoItems: LazyPagingItems<MemoListUiState>,
    onMemoFinish: (String) -> Unit,
    onMemoDelete: (String) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TitleTopBar(title = "메모") },
        floatingActionButton = { AddFloatingButton(onAdd = onAdd) }
    ) {
        Content(
            modifier = Modifier.padding(it)
                .fillMaxSize(),
            memoItems = memoItems,
            onMemoFinish = onMemoFinish,
            onMemoDelete = onMemoDelete,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    memoItems: LazyPagingItems<MemoListUiState>,
    onMemoFinish: (String) -> Unit,
    onMemoDelete: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(
            count = memoItems.itemCount,
            key = memoItems.itemKey { it.id },
            contentType = { "Memo" },
        ) {
            SwipeMemo(
                modifier = Modifier.fillParentMaxWidth(),
                uiState = memoItems[it],
                onMemoFinish = onMemoFinish,
                onMemoDelete = onMemoDelete,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeMemo(
    modifier: Modifier = Modifier,
    uiState: MemoListUiState?,
    onMemoFinish: (String) -> Unit,
    onMemoDelete: (String) -> Unit,
) {
    val state = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmValueChange = {
            when (it) {
                DismissValue.DismissedToStart -> {
                    uiState?.id?.let(onMemoFinish)
                }

                DismissValue.DismissedToEnd -> {
                    uiState?.id?.let(onMemoDelete)
                }

                else -> Unit
            }

            uiState != null
        }
    )

    SwipeToDismiss(
        modifier = modifier.clip(MemoDefaults.shape),
        state = state,
        background = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .drawBehind {
                        val backgroundColor = when (state.dismissDirection) {
                            DismissDirection.EndToStart -> Color.Red
                            DismissDirection.StartToEnd -> Color.Green
                            else -> null
                        }?.copy(alpha = 0.5F)

                        backgroundColor?.let(::drawRect)
                    }
                    .padding(horizontal = 8.dp)
            ) {
                when (state.dismissDirection) {
                    DismissDirection.EndToStart -> SwipeDeleteButton(state = state)
                    DismissDirection.StartToEnd -> SwipeFinishButton(state = state)
                    else -> Unit
                }
            }
        },
        dismissContent = {
            Memo(
                modifier = Modifier.fillMaxWidth(),
                uiState = uiState
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.SwipeDeleteButton(
    modifier: Modifier = Modifier,
    state: DismissState,
) {
    SwipeIcon(
        state = state,
        dismissValue = DismissValue.DismissedToStart,
    ) { align, size ->
        DeleteIcon(
            modifier = modifier.align(align)
                .size(size)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.SwipeFinishButton(
    modifier: Modifier = Modifier,
    state: DismissState,
) {
    SwipeIcon(
        state = state,
        dismissValue = DismissValue.DismissedToEnd,
    ) { align, size ->
        FinishIcon(
            modifier = modifier.align(align)
                .size(size)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.SwipeIcon(
    state: DismissState,
    dismissValue: DismissValue,
    content: @Composable (align: Alignment, size: Dp) -> Unit,
) {
    val alignment = when (dismissValue) {
        DismissValue.DismissedToEnd -> Alignment.CenterStart
        DismissValue.DismissedToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }
    val size by animateDpAsState(
        when (state.targetValue) {
            dismissValue -> 32.dp
            else -> 16.dp
        }
    )

    when (state.targetValue) {
        dismissValue -> content(alignment, size)

        DismissValue.Default -> CircleIcon(
            modifier = Modifier.align(alignment)
                .size(size)
        )

        else -> Unit
    }
}

@Composable
private fun Memo(
    modifier: Modifier = Modifier,
    uiState: MemoListUiState?,
    shape: Shape = MemoDefaults.shape
) {
    Card(
        modifier = modifier,
        shape = shape,
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = uiState?.title.orEmpty()
        )
    }
}