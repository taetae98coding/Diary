package com.taetae98.diary.feature.memo.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.button.CheckFloatingButton
import com.taetae98.diary.ui.compose.icon.DeleteIcon
import com.taetae98.diary.ui.compose.icon.FinishIcon
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.entity.EntityDetail

@Composable
internal fun MemoDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    uiState: State<MemoDetailUiState>,
    toolbarUiState: State<MemoDetailToolbarUiState>,
    titleUiState: State<TextFieldUiState>,
) {
    val hostState = remember { SnackbarHostState() }

    DiaryScaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                onNavigateUp = onNavigateUp,
                toolbarUiState = toolbarUiState,
            )
        },
        floatingActionButton = {
            FloatingButton(uiState = uiState)
        },
        snackbarHost = {
            SnackbarHost(hostState = hostState)
        },
    ) {
        Content(
            modifier = Modifier.padding(it),
            titleUiState = titleUiState,
        )
    }

    Message(
        uiState = uiState,
        hostState = hostState,
    )
}

@Composable
internal fun TopBar(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    toolbarUiState: State<MemoDetailToolbarUiState>,
) {
    NavigateUpTopBar(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        actions = {
            when (val value = toolbarUiState.value) {
                is MemoDetailToolbarUiState.Detail -> {
                    IconButton(onClick = value.onComplete) {
                        val tint = if (value.isComplete) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            LocalContentColor.current
                        }

                        FinishIcon(tint = tint)
                    }
                    IconButton(onClick = value.onDelete) {
                        DeleteIcon()
                    }
                }

                else -> Unit
            }
        }
    )
}

@Composable
private fun Message(
    uiState: State<MemoDetailUiState>,
    hostState: SnackbarHostState,
) {
    LaunchedEffect(uiState.value) {
        when (uiState.value.message) {
            MemoDetailMessage.Add -> {
                hostState.showSnackbar("메모 추가")
                uiState.value.onMessageShown()
            }

            MemoDetailMessage.TitleEmpty -> {
                hostState.showSnackbar("제목을 입력해주세요.")
                uiState.value.onMessageShown()
            }

            else -> Unit
        }
    }
}

@Composable
private fun FloatingButton(
    modifier: Modifier = Modifier,
    uiState: State<MemoDetailUiState>,
) {
    when (val value = uiState.value) {
        is MemoDetailUiState.Add -> {
            AddFloatingButton(
                modifier = modifier,
                onClick = value.onAdd
            )
        }

        is MemoDetailUiState.Detail -> {
            CheckFloatingButton(
                modifier = modifier,
                onClick = value.onUpdate
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    titleUiState: State<TextFieldUiState>,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        EntityDetail(
            modifier = Modifier.fillMaxWidth(),
            titleUiState = titleUiState
        )
    }
}
