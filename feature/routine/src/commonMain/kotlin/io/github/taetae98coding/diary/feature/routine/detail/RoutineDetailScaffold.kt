package io.github.taetae98coding.diary.feature.routine.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import io.github.taetae98coding.diary.compose.core.button.CheckFloatingButton
import io.github.taetae98coding.diary.compose.core.button.NavigateUpButton
import io.github.taetae98coding.diary.compose.core.card.ColorCard
import io.github.taetae98coding.diary.compose.core.card.DescriptionCard
import io.github.taetae98coding.diary.compose.core.card.TitleCard
import io.github.taetae98coding.diary.compose.core.icon.DeleteIcon
import io.github.taetae98coding.diary.compose.core.icon.FinishIcon
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.feature.routine.add.card.RRuleCard
import io.github.taetae98coding.diary.feature.routine.add.card.RoutineCountCard
import io.github.taetae98coding.diary.feature.routine.add.card.RoutineDateRangeCard

@Composable
internal fun RoutineDetailScaffold(
    state: RoutineDetailScaffoldState,
    modifier: Modifier = Modifier,
    detailProvider: () -> RoutineDetail? = { null },
    finishUiStateProvider: () -> RoutineDetailFinishUiState = { RoutineDetailFinishUiState() },
    deleteUiStateProvider: () -> RoutineDetailDeleteUiState = { RoutineDetailDeleteUiState() },
    updateInProgressProvider: () -> Boolean = { false },
    rRulesProvider: () -> List<RoutineRRule> = { emptyList() },
    onNavigateUp: () -> Unit = {},
    onUpdate: () -> Unit = {},
    onAddRRule: (List<RoutineRRule>) -> Unit = {},
    onRemoveRRule: (RoutineRRule) -> Unit = {},
    onFinish: () -> Unit = {},
    onRestart: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .focusableKeyEvent(autoFocus = false) { event ->
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
                    detail != null && state.detail != detail && state.isFabVisible
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
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            state = state.lazyListState,
            contentPadding = DiaryTheme.dimen.screenPaddingValues,
            verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.screenCardSpace),
        ) {
            item { TitleCard(state = state.titleCardState) }
            item { DescriptionCard(state = state.descriptionCardState) }
            item { ColorCard(state = state.colorCardState, modifier = Modifier.fillParentMaxWidth()) }
            item { RoutineDateRangeCard(state = state.dateRangeCardState, modifier = Modifier.fillParentMaxWidth()) }
            item {
                RRuleCard(
                    rRulesProvider = rRulesProvider,
                    onAdd = onAddRRule,
                    onRemove = onRemoveRRule,
                    modifier = Modifier.fillParentMaxWidth(),
                )
            }
            item {
                RoutineCountCard(
                    state = state.routineCountCardState,
                    modifier = Modifier.fillParentMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    detailProvider: () -> RoutineDetail?,
    finishUiStateProvider: () -> RoutineDetailFinishUiState,
    deleteUiStateProvider: () -> RoutineDetailDeleteUiState,
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
                uiStateProvider = finishUiStateProvider,
                onFinish = onFinish,
                onRestart = onRestart,
            )
            DeleteButton(
                uiStateProvider = deleteUiStateProvider,
                onDelete = onDelete,
            )
        },
    )
}

@Composable
private fun FinishButton(
    uiStateProvider: () -> RoutineDetailFinishUiState,
    onFinish: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState = uiStateProvider()

    AnimatedContent(targetState = uiState.isInProgress, modifier = modifier) { isInProgress ->
        if (isInProgress) {
            CircularWavyProgressIndicator(modifier = Modifier.size(24.dp))
        } else {
            IconToggleButton(
                checked = uiState.isFinished,
                onCheckedChange = { checked ->
                    if (checked) onFinish() else onRestart()
                },
            ) {
                FinishIcon()
            }
        }
    }
}

@Composable
private fun DeleteButton(
    uiStateProvider: () -> RoutineDetailDeleteUiState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState = uiStateProvider()

    Crossfade(targetState = uiState.isInProgress, modifier = modifier) { isInProgress ->
        if (isInProgress) {
            CircularWavyProgressIndicator(modifier = Modifier.size(24.dp))
        } else {
            IconButton(onClick = onDelete) {
                DeleteIcon()
            }
        }
    }
}

@ScreenPreview
@Composable
private fun Preview() {
    DiaryTheme {
        RoutineDetailScaffold(
            state = rememberRoutineDetailScaffoldState(routineProvider = { null }),
            detailProvider = {
                RoutineDetail(
                    title = "루틴 제목",
                    description = "",
                    start = null,
                    endInclusive = null,
                    color = 0,
                    routineCount = 1,
                )
            },
        )
    }
}
