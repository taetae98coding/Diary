package io.github.taetae98coding.diary.feature.tag.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TagListScreen(
    state: TagListScreenState,
    floatingButtonProvider: () -> TagListFloatingButton,
    listProvider: () -> List<TagListItemUiState>?,
    onTag: (String) -> Unit,
    uiStateProvider: () -> TagListScreenUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "태그") },
            )
        },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
        floatingActionButton = {
            val button = floatingButtonProvider()

            AnimatedVisibility(
                visible = button is TagListFloatingButton.Add,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                FloatingAddButton(
                    onClick = {
                        if (button is TagListFloatingButton.Add) {
                            button.onAdd()
                        }
                    },
                )
            }
        },
    ) {
        Content(
            listProvider = listProvider,
            onTag = onTag,
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
    state: TagListScreenState,
    uiStateProvider: () -> TagListScreenUiState,
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
                    message = "태그 완료 ${Emoji.congratulate.random()}",
                    actionLabel = "취소",
                ) {
                    uiState.restartTag(uiState.finishTagId)
                }
            }

            !uiState.deleteTagId.isNullOrBlank() -> {
                state.showMessage(
                    message = "태그 삭제 ${Emoji.congratulate.random()}",
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
    listProvider: () -> List<TagListItemUiState>?,
    onTag: (String) -> Unit,
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
                contentType = { "Tag" },
            ) {
                TagItem(
                    uiState = null,
                    onClick = { },
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
                        text = "태그가 없어요 🐼",
                        style = DiaryTheme.typography.headlineMedium,
                    )
                }
            }
        } else {
            items(
                items = listProvider().orEmpty(),
                key = { it.id },
                contentType = { "Tag" },
            ) {
                TagItem(
                    uiState = it,
                    onClick = { onTag(it.id) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun TagItem(
    uiState: TagListItemUiState?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FinishAndDeleteSwipeBox(
        modifier = modifier,
        onFinish = { uiState?.finish?.value?.invoke() },
        onDelete = { uiState?.delete?.value?.invoke() },
    ) {
        Card(onClick = onClick) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = uiState?.title.orEmpty(),
                    style = DiaryTheme.typography.titleLarge,
                )
            }
        }
    }
}
