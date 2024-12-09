package io.github.taetae98coding.diary.feature.tag.memo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.compose.button.FloatingAddButton
import io.github.taetae98coding.diary.core.compose.swipe.FinishAndDeleteSwipeBox
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.library.color.multiplyAlpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TagMemoScreen(
    state: TagMemoScreenState,
    navigateButtonProvider: () -> TagMemoNavigateButton,
    uiStateProvider: () -> TagMemoScreenUiState,
    onAdd: () -> Unit,
    listProvider: () -> List<MemoListItemUiState>?,
    onMemo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    when (val button = navigateButtonProvider()) {
                        is TagMemoNavigateButton.NavigateUp -> {
                            IconButton(onClick = button.onNavigateUp) {
                                NavigateUpIcon()
                            }
                        }

                        is TagMemoNavigateButton.None -> Unit
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
        floatingActionButton = { FloatingAddButton(onClick = onAdd) },
    ) {
        Content(
            listProvider = listProvider,
            onMemo = onMemo,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        )
    }

    Message(
        state = state,
        uiStateProvider = uiStateProvider,
    )
}

@Composable
private fun Message(
    state: TagMemoScreenState,
    uiStateProvider: () -> TagMemoScreenUiState,
) {
    val uiState = uiStateProvider()

    LaunchedEffect(
        uiState.finishTagId,
        uiState.deleteTagId,
        uiState.isUnknownError,
    ) {
        if (!uiState.hasMessage) return@LaunchedEffect

        when {
            !uiState.finishTagId.isNullOrBlank() -> {
                state.showMessage(
                    message = "메모 완료 ${Emoji.congratulate.random()}",
                    actionLabel = "취소",
                ) {
                    uiState.restartTag(uiState.finishTagId)
                }
            }

            !uiState.deleteTagId.isNullOrBlank() -> {
                state.showMessage(
                    message = "메모 삭제 ${Emoji.congratulate.random()}",
                    actionLabel = "취소",
                ) {
                    uiState.restoreTag(uiState.deleteTagId)
                }
            }

            uiState.isUnknownError -> state.showMessage("알 수 없는 에러가 발생했어요 잠시 후 다시 시도해 주세요 ${Emoji.error.random()}")
        }

        uiState.onMessageShow()
    }
}

@Composable
private fun Content(
    listProvider: () -> List<MemoListItemUiState>?,
    onMemo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = DiaryTheme.dimen.screenPaddingValues,
        verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
    ) {
        val list = listProvider()

        if (list == null) {
            items(
                count = 5,
                contentType = { "Memo" },
            ) {
                MemoItem(
                    uiState = null,
                    onClick = {},
                    modifier = Modifier.animateItem(),
                )
            }
        } else if (list.isEmpty()) {
            item(
                key = "Loading",
                contentType = "Loading",
            ) {
                Box(
                    modifier = Modifier.fillParentMaxSize()
                        .animateItem(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "메모가 없어요 🐰",
                        style = DiaryTheme.typography.headlineMedium,
                    )
                }
            }
        } else {
            items(
                items = listProvider().orEmpty(),
                key = { it.id },
                contentType = { "Memo" },
            ) {
                MemoItem(
                    uiState = it,
                    onClick = { onMemo(it.id) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun MemoItem(
    uiState: MemoListItemUiState?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FinishAndDeleteSwipeBox(
        modifier = modifier,
        onFinish = { uiState?.finish?.value?.invoke() },
        onDelete = { uiState?.delete?.value?.invoke() },
    ) {
        Card(onClick = onClick) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                MaterialTheme.typography.bodySmall
                Text(
                    text = uiState?.title.orEmpty(),
                    style = DiaryTheme.typography.titleLarge,
                )
                if (uiState?.dateRange != null) {
                    Text(
                        text = listOf(uiState.dateRange.start, uiState.dateRange.endInclusive)
                            .distinct()
                            .joinToString(separator = " ~ "),
                        color = LocalContentColor.current.multiplyAlpha(0.5F),
                        style = DiaryTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}
