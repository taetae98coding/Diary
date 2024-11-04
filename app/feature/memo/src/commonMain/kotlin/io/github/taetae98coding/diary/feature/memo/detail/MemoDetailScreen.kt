package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.diary.color.DiaryColor
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponent
import io.github.taetae98coding.diary.core.design.system.diary.date.DiaryDate
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.resources.Res
import io.github.taetae98coding.diary.core.resources.icon.AddIcon
import io.github.taetae98coding.diary.core.resources.icon.DeleteIcon
import io.github.taetae98coding.diary.core.resources.icon.FinishIcon
import io.github.taetae98coding.diary.core.resources.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.resources.memo_add_message
import io.github.taetae98coding.diary.core.resources.title_blank_error
import io.github.taetae98coding.diary.core.resources.unknown_error
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MemoDetailScreen(
    state: MemoDetailScreenState,
    titleProvider: () -> String?,
    navigateButtonProvider: () -> MemoDetailNavigationButton,
    actionButtonProvider: () -> MemoDetailActionButton,
    floatingButtonProvider: () -> MemoDetailFloatingButton,
    uiStateProvider: () -> MemoDetailScreenUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    titleProvider()?.let {
                        Text(
                            text = it,
                            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                            maxLines = 1,
                        )
                    }
                },
                navigationIcon = {
                    when (val button = navigateButtonProvider()) {
                        is MemoDetailNavigationButton.NavigateUp -> {
                            IconButton(onClick = button.onNavigateUp) {
                                NavigateUpIcon()
                            }
                        }

                        else -> Unit
                    }
                },
                actions = {
                    when (val button = actionButtonProvider()) {
                        is MemoDetailActionButton.FinishAndDetail -> {
                            IconToggleButton(
                                checked = button.isFinish,
                                onCheckedChange = button.onFinishChange,
                            ) {
                                FinishIcon()
                            }

                            IconButton(onClick = button.delete) {
                                DeleteIcon()
                            }
                        }

                        else -> Unit
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
        floatingActionButton = {
            when (val button = floatingButtonProvider()) {
                is MemoDetailFloatingButton.Add -> {
                    val isProgress by remember { derivedStateOf { uiStateProvider().isProgress } }

                    FloatingActionButton(onClick = button.onAdd) {
                        if (isProgress) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            AddIcon()
                        }
                    }
                }

                else -> Unit
            }
        },
    ) {
        Content(
            state = state,
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .padding(DiaryTheme.dimen.screenPaddingValues),
        )
    }

    Message(
        state = state,
        uiStateProvider = uiStateProvider,
    )

    LaunchedFocus(state = state)
}

@Composable
private fun Message(
    state: MemoDetailScreenState,
    uiStateProvider: () -> MemoDetailScreenUiState,
) {
    val uiState = uiStateProvider()
    val addMessage = stringResource(Res.string.memo_add_message)
    val titleBlankMessage = stringResource(Res.string.title_blank_error)
    val unknownErrorMessage = stringResource(Res.string.unknown_error)

    LaunchedEffect(
        uiState.isAdd,
        uiState.isDelete,
        uiState.isUpdate,
        uiState.isTitleBlankError,
        uiState.isUnknownError,
    ) {
        if (!uiState.hasMessage) return@LaunchedEffect

        when {
            uiState.isAdd -> {
                state.showMessage(addMessage)
                state.clearInput()
                state.requestTitleFocus()
            }

            uiState.isDelete -> {
                if (state is MemoDetailScreenState.Detail) {
                    state.onDelete()
                }
            }

            uiState.isUpdate -> {
                if (state is MemoDetailScreenState.Detail) {
                    state.onUpdate()
                }
            }

            uiState.isTitleBlankError -> {
                state.showMessage(titleBlankMessage)
                state.titleError()
            }

            uiState.isUnknownError -> state.showMessage(unknownErrorMessage)
        }

        uiState.onMessageShow()
    }
}

@Composable
private fun LaunchedFocus(
    state: MemoDetailScreenState,
) {
    LaunchedEffect(state) {
        if (state is MemoDetailScreenState.Add) {
            state.requestTitleFocus()
        }
    }
}

@Composable
private fun Content(
    state: MemoDetailScreenState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.verticalScroll(state = rememberScrollState())
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
    ) {
        DiaryComponent(state = state.componentState)
        DiaryDate(state = state.dateState)
        Row {
            DiaryColor(
                state = state.colorState,
                modifier = Modifier.weight(1F)
                    .height(100.dp),
            )

            Spacer(modifier = Modifier.weight(1F))
        }
    }
}
