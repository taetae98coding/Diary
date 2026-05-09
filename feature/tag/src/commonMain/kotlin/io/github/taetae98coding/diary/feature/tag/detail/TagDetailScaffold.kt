@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
)

package io.github.taetae98coding.diary.feature.tag.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
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
import io.github.taetae98coding.diary.compose.core.padding.plus
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.scaffold.DiaryScaffold
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.tag.TagDetail

@Composable
internal fun TagDetailScaffold(
    state: TagDetailScaffoldState,
    detailProvider: () -> TagDetail?,
    finishUiStateProvider: () -> TagDetailFinishUiState,
    deleteUiStateProvider: () -> TagDetailDeleteUiState,
    updateInProgressProvider: () -> Boolean,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onNavigateToTagMemo: () -> Unit = {},
    onUpdate: () -> Unit = {},
    onFinish: () -> Unit = {},
    onRestart: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    val isLoading by remember { derivedStateOf { detailProvider() == null } }

    DiaryScaffold(
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
                    contentPadding = DiaryTheme.dimen.screenPaddingValues + WindowInsets.navigationBars.asPaddingValues(),
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
                        Card(
                            onClick = dropUnlessResumed(block = onNavigateToTagMemo),
                            modifier = Modifier.size(64.dp),
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                MemoIcon()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    detailProvider: () -> TagDetail?,
    finishUiStateProvider: () -> TagDetailFinishUiState,
    deleteUiStateProvider: () -> TagDetailDeleteUiState,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onFinish: () -> Unit = {},
    onRestart: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    TopAppBar(
        title = {
            detailProvider()
                ?.let {
                    Text(
                        text = it.title,
                        modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                        maxLines = 1,
                    )
                }
        },
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
private fun FinishButton(
    finishUiStateProvider: () -> TagDetailFinishUiState,
    modifier: Modifier = Modifier,
    onFinish: () -> Unit = {},
    onRestart: () -> Unit = {},
) {
    AnimatedContent(
        targetState = finishUiStateProvider(),
        modifier = modifier,
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Crossfade(targetState = deleteUiStateProvider().isInProgress, modifier = modifier) { isInProgress ->
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

@ScreenPreview
@Composable
private fun Preview() {
    val state = rememberTagDetailScaffoldState(tagProvider = { null })
    val detail = TagDetail(
        title = "태그 제목",
        description = "",
        color = 0,
    )

    DiaryTheme {
        TagDetailScaffold(
            state = state,
            detailProvider = { detail },
            finishUiStateProvider = { TagDetailFinishUiState() },
            deleteUiStateProvider = { TagDetailDeleteUiState() },
            updateInProgressProvider = { false },
        )
    }
}
