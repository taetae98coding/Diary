package io.github.taetae98coding.diary.feature.routine.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.lifecycle.compose.dropUnlessResumed
import io.github.taetae98coding.diary.compose.core.button.AddFloatingButton
import io.github.taetae98coding.diary.compose.core.button.NavigateUpButton
import io.github.taetae98coding.diary.compose.core.card.ColorCard
import io.github.taetae98coding.diary.compose.core.card.DescriptionCard
import io.github.taetae98coding.diary.compose.core.card.TitleCard
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.routine.add.card.RRuleCard
import io.github.taetae98coding.diary.feature.routine.add.card.RoutineCountCard
import io.github.taetae98coding.diary.feature.routine.add.card.RoutineDateRangeCard

@Composable
internal fun RoutineAddScaffold(
    state: RoutineAddScaffoldState,
    modifier: Modifier = Modifier,
    isInProgressProvider: () -> Boolean = { false },
    onNavigateUp: () -> Unit = {},
    onAdd: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .focusableKeyEvent(autoFocus = false) { event ->
                if (event.type == KeyEventType.KeyDown && event.isMetaPressed && event.key == Key.Enter) {
                    onAdd()
                    true
                } else {
                    false
                }
            }.imePadding(),
        topBar = { TopBar(onNavigateUp = onNavigateUp) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.isFabVisible,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                AddFloatingButton(
                    onClick = dropUnlessResumed(block = onAdd),
                    isInProgressProvider = isInProgressProvider,
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
                RoutineDateRangeCard(
                    state = state.dateRangeCardState,
                    modifier = Modifier.fillParentMaxWidth(),
                )
            }

            item {
                RoutineCountCard(
                    state = state.routineCountCardState,
                    modifier = Modifier.fillParentMaxWidth(),
                )
            }

            item {
                RRuleCard(
                    state = state.rRuleCardState,
                    modifier = Modifier.fillParentMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = "루틴 추가") },
        modifier = modifier,
        navigationIcon = { NavigateUpButton(onClick = dropUnlessResumed(block = onNavigateUp)) },
    )
}

@ScreenPreview
@Composable
private fun Preview() {
    DiaryTheme {
        RoutineAddScaffold(state = rememberRoutineAddScaffoldState())
    }
}

@ScreenPreview
@Composable
private fun InProgressPreview() {
    DiaryTheme {
        RoutineAddScaffold(
            state = rememberRoutineAddScaffoldState(),
            isInProgressProvider = { true },
        )
    }
}
