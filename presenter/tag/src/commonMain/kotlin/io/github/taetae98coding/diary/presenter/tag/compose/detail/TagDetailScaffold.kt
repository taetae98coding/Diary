package io.github.taetae98coding.diary.presenter.tag.compose.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import io.github.taetae98coding.diary.compose.core.button.CheckFloatingButton
import io.github.taetae98coding.diary.compose.core.button.NavigateUpButton
import io.github.taetae98coding.diary.compose.core.card.ColorCard
import io.github.taetae98coding.diary.compose.core.card.DescriptionCard
import io.github.taetae98coding.diary.compose.core.card.TitleCard
import io.github.taetae98coding.diary.compose.core.icon.DeleteIcon
import io.github.taetae98coding.diary.compose.core.icon.FinishIcon
import io.github.taetae98coding.diary.compose.core.icon.MemoIcon
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.presenter.tag.api.TagDetailDeleteUiState
import io.github.taetae98coding.diary.presenter.tag.api.TagDetailEffect
import io.github.taetae98coding.diary.presenter.tag.api.TagDetailFinishUiState
import io.github.taetae98coding.diary.presenter.tag.api.TagDetailStateHolder
import kotlinx.coroutines.launch

@Composable
public fun TagDetailScaffold(
    stateHolder: TagDetailStateHolder,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onNavigateToTagMemo: () -> Unit = {},
) {
    val updateInProgress by stateHolder.updateInProgress.collectAsStateWithLifecycle()
    val finishUiState by stateHolder.finishUiState.collectAsStateWithLifecycle()
    val deleteUiState by stateHolder.deleteUiState.collectAsStateWithLifecycle()
    val tag by stateHolder.tag.collectAsStateWithLifecycle()
    val state = rememberTagDetailScaffoldState(tagProvider = { tag })

    TagDetailScaffold(
        state = state,
        detailProvider = { tag?.detail },
        finishUiStateProvider = { finishUiState },
        deleteUiStateProvider = { deleteUiState },
        updateInProgressProvider = { updateInProgress },
        onNavigateUp = onNavigateUp,
        onNavigateToTagMemo = onNavigateToTagMemo,
        onUpdate = { stateHolder.update(state.detail) },
        onFinish = stateHolder::finish,
        onRestart = stateHolder::restart,
        onDelete = stateHolder::delete,
        modifier = modifier,
    )

    TagDetailEffectHandler(
        stateHolder = stateHolder,
        state = state,
        onNavigateUp = onNavigateUp,
    )
}

@Composable
internal fun TagDetailScaffold(
    state: TagDetailScaffoldState,
    detailProvider: () -> TagDetail?,
    finishUiStateProvider: () -> TagDetailFinishUiState,
    deleteUiStateProvider: () -> TagDetailDeleteUiState,
    updateInProgressProvider: () -> Boolean,
    onNavigateUp: () -> Unit,
    onNavigateToTagMemo: () -> Unit,
    onUpdate: () -> Unit,
    onFinish: () -> Unit,
    onRestart: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLoading by remember { derivedStateOf { detailProvider() == null } }

    Scaffold(
        modifier = modifier
            .focusableKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown && event.isMetaPressed && event.key == Key.Enter) {
                    onUpdate()
                    true
                } else {
                    false
                }
            }.imePadding(),
        topBar = {
            TopBar(
                detailProvider = detailProvider,
                finishUiStateProvider = finishUiStateProvider,
                deleteUiStateProvider = deleteUiStateProvider,
                onNavigateUp = onNavigateUp,
                onFinish = onFinish,
                onRestart = onRestart,
                onDelete = onDelete,
            )
        },
        floatingActionButton = {
            val isVisible by remember(state) {
                derivedStateOf {
                    val detail = detailProvider()
                    detail != null && state.detail != detail
                }
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                CheckFloatingButton(
                    onClick = dropUnlessResumed(block = onUpdate),
                    isInProgressProvider = updateInProgressProvider,
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
    ) { paddingValues ->
        Crossfade(
            targetState = isLoading,
            modifier = Modifier
                .padding(paddingValues),
        ) { isLoading ->
            if (!isLoading) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = DiaryTheme.dimen.screenPaddingValues,
                    verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.screenCardSpace),
                ) {
                    item {
                        TitleCard(state = state.titleCardState)
                    }

                    item {
                        DescriptionCard(state = state.descriptionCardState)
                    }

                    item {
                        ColorCard(
                            state = state.colorCardState,
                            modifier = Modifier.fillParentMaxWidth(),
                        )
                    }

                    item {
                        MemoCard(
                            onClick = dropUnlessResumed(block = onNavigateToTagMemo),
                            modifier = Modifier.fillParentMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TagDetailEffectHandler(
    stateHolder: TagDetailStateHolder,
    state: TagDetailScaffoldState,
    onNavigateUp: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val effect by stateHolder.effect.collectAsStateWithLifecycle()

    LaunchedEffect(stateHolder, state, effect) {
        when (effect) {
            TagDetailEffect.UpdateFinish -> {
                coroutineScope.launch { state.hostState.showImmediate("태그가 수정되었습니다") }
                stateHolder.consumeEffect()
            }

            TagDetailEffect.DeleteFinish -> {
                onNavigateUp()
                stateHolder.consumeEffect()
            }

            TagDetailEffect.UnknownError -> {
                coroutineScope.launch { state.hostState.showImmediate("네트워크 상태를 확인하세요") }
                stateHolder.consumeEffect()
            }

            TagDetailEffect.None -> Unit
        }
    }
}

@Composable
private fun TopBar(
    detailProvider: () -> TagDetail?,
    finishUiStateProvider: () -> TagDetailFinishUiState,
    deleteUiStateProvider: () -> TagDetailDeleteUiState,
    onNavigateUp: () -> Unit,
    onFinish: () -> Unit,
    onRestart: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { detailProvider()?.let { Text(text = it.title) } },
        modifier = modifier,
        navigationIcon = { NavigateUpButton(onClick = dropUnlessResumed(block = onNavigateUp)) },
        actions = {
            FinishButton(
                finishUiStateProvider = finishUiStateProvider,
                onFinish = dropUnlessResumed(block = onFinish),
                onRestart = dropUnlessResumed(block = onRestart),
            )
            DeleteButton(
                deleteUiStateProvider = deleteUiStateProvider,
                onClick = dropUnlessResumed(block = onDelete),
            )
        },
    )
}

@Composable
private fun MemoCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MemoIcon()
            Text(
                text = "메모 보기",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun FinishButton(
    finishUiStateProvider: () -> TagDetailFinishUiState,
    onFinish: () -> Unit,
    onRestart: () -> Unit,
) {
    AnimatedContent(
        targetState = finishUiStateProvider(),
        contentKey = { it.isInProgress },
    ) { uiState ->
        if (uiState.isInProgress) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clearAndSetSemantics { },
                contentAlignment = Alignment.Center,
            ) {
                CircularWavyProgressIndicator(modifier = Modifier.size(24.dp))
            }
        } else {
            IconToggleButton(
                checked = uiState.isFinished,
                onCheckedChange = { checked ->
                    if (checked) {
                        onFinish()
                    } else {
                        onRestart()
                    }
                },
            ) {
                FinishIcon()
            }
        }
    }
}

@Composable
private fun DeleteButton(
    deleteUiStateProvider: () -> TagDetailDeleteUiState,
    onClick: () -> Unit,
) {
    Crossfade(targetState = deleteUiStateProvider().isInProgress) { isInProgress ->
        if (isInProgress) {
            IconButton(onClick = {}) {
                CircularWavyProgressIndicator(modifier = Modifier.size(24.dp))
            }
        } else {
            IconButton(onClick = onClick) {
                DeleteIcon()
            }
        }
    }
}
