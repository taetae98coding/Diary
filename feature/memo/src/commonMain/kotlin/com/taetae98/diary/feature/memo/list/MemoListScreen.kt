package com.taetae98.diary.feature.memo.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
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
    onNavigateToMemoDetail: (memoId: String) -> Unit,
    onMemoComplete: (memoId: String) -> Unit,
    onMemoDelete: (memoId: String) -> Unit,
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
            onNavigateToMemoDetail = onNavigateToMemoDetail,
            onMemoComplete = onMemoComplete,
            onMemoDelete = onMemoDelete,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    memoItems: LazyPagingItems<MemoListUiState>,
    onNavigateToMemoDetail: (memoId: String) -> Unit,
    onMemoComplete: (memoId: String) -> Unit,
    onMemoDelete: (memoId: String) -> Unit,
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
            val uiState = memoItems[it]

            SwipeMemo(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable(
                        enabled = uiState != null,
                        onClickLabel = uiState?.title,
                        onClick = { uiState?.id?.let(onNavigateToMemoDetail) }
                    ),
                uiState = uiState,
                onNavigateToMemoDetail = onNavigateToMemoDetail,
                onMemoComplete = onMemoComplete,
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
    onNavigateToMemoDetail: (memoId: String) -> Unit,
    onMemoComplete: (memoId: String) -> Unit,
    onMemoDelete: (memoId: String) -> Unit,
) {
    val state = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmValueChange = {
            when (it) {
                DismissValue.DismissedToStart -> {
                    uiState?.id?.let(onMemoDelete)
                }

                DismissValue.DismissedToEnd -> {
                    uiState?.id?.let(onMemoComplete)
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
        modifier = modifier,
        state = state,
        dismissValue = DismissValue.DismissedToStart,
    ) {
        DeleteIcon()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.SwipeFinishButton(
    modifier: Modifier = Modifier,
    state: DismissState,
) {
    SwipeIcon(
        modifier = modifier,
        state = state,
        dismissValue = DismissValue.DismissedToEnd,
    ) {
        FinishIcon()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.SwipeIcon(
    modifier: Modifier = Modifier,
    state: DismissState,
    dismissValue: DismissValue,
    content: @Composable () -> Unit,
) {
    val alignment = when (dismissValue) {
        DismissValue.DismissedToEnd -> Alignment.CenterStart
        DismissValue.DismissedToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }

    val scale by animateFloatAsState(
        when (state.targetValue) {
            dismissValue -> 1.33F
            else -> 0.66F
        }
    )

    when (state.targetValue) {
        dismissValue -> Box(
            modifier = modifier.align(alignment)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            content()
        }

        DismissValue.Default -> CircleIcon(
            modifier = modifier.align(alignment)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
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