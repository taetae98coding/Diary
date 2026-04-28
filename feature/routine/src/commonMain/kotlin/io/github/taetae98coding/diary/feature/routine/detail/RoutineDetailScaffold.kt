package io.github.taetae98coding.diary.feature.routine.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
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
import androidx.lifecycle.compose.dropUnlessResumed
import io.github.taetae98coding.diary.compose.core.button.CheckFloatingButton
import io.github.taetae98coding.diary.compose.core.button.NavigateUpButton
import io.github.taetae98coding.diary.compose.core.card.ColorCard
import io.github.taetae98coding.diary.compose.core.card.DescriptionCard
import io.github.taetae98coding.diary.compose.core.card.LocalDateRangeCard
import io.github.taetae98coding.diary.compose.core.card.TitleCard
import io.github.taetae98coding.diary.compose.core.icon.AddIcon
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.feature.routine.add.component.CalendarVisibilityCard
import io.github.taetae98coding.diary.feature.routine.add.component.RRuleEditorDialog
import io.github.taetae98coding.diary.feature.routine.add.component.RRuleSummaryCard
import io.github.taetae98coding.diary.feature.routine.add.component.RoutineCountEditor
import io.github.taetae98coding.diary.feature.routine.detail.component.RoutineDateListCard

@Composable
internal fun RoutineDetailScaffold(
    state: RoutineDetailScaffoldState,
    routineProvider: () -> Routine?,
    updateInProgressProvider: () -> Boolean,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onUpdate: () -> Unit = {},
) {
    val isLoading by remember { derivedStateOf { routineProvider() == null } }

    Scaffold(
        modifier = modifier
            .focusableKeyEvent(autoFocus = false) { event ->
                if (event.type == KeyEventType.KeyDown && event.isMetaPressed && event.key == Key.Enter) {
                    onUpdate()
                    true
                } else {
                    false
                }
            }
            .imePadding(),
        topBar = {
            TopBar(
                routineProvider = routineProvider,
                onNavigateUp = onNavigateUp,
            )
        },
        floatingActionButton = {
            val isVisible by remember(state) {
                derivedStateOf {
                    val routine = routineProvider() ?: return@derivedStateOf false
                    routine.detail != state.detail ||
                        routine.rRules != state.rRules.toList() ||
                        routine.rDates != state.rDates.toSet() ||
                        routine.exDates != state.exDates.toSet() ||
                        routine.isCalendarVisible != state.calendarVisibilityState.isVisible
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
            modifier = Modifier.padding(paddingValues),
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
                        LocalDateRangeCard(
                            state = state.localDateRangeCardState,
                            modifier = Modifier.fillParentMaxWidth(),
                        )
                    }
                    item {
                        RoutineCountEditor(
                            state = state.routineCountState,
                            modifier = Modifier.fillParentMaxWidth(),
                        )
                    }
                    item {
                        CalendarVisibilityCard(
                            state = state.calendarVisibilityState,
                            modifier = Modifier.fillParentMaxWidth(),
                        )
                    }
                    items(items = state.rRules) { rule ->
                        RRuleSummaryCard(
                            rRule = rule,
                            onDelete = { state.removeRule(rule) },
                            modifier = Modifier.fillParentMaxWidth(),
                        )
                    }
                    item {
                        FilledTonalButton(
                            onClick = state::showRRuleDialog,
                            modifier = Modifier.fillParentMaxWidth(),
                        ) {
                            AddIcon()
                            Text(text = "규칙 추가")
                        }
                    }
                    item {
                        RoutineDateListCard(
                            title = "추가 날짜",
                            dates = state.rDates,
                            onAdd = state::addRDate,
                            onRemove = state::removeRDate,
                            modifier = Modifier.fillParentMaxWidth(),
                        )
                    }
                    item {
                        RoutineDateListCard(
                            title = "제외 날짜",
                            dates = state.exDates,
                            onAdd = state::addExDate,
                            onRemove = state::removeExDate,
                            modifier = Modifier.fillParentMaxWidth(),
                        )
                    }
                }
            }
        }
    }

    RRuleEditorDialog(
        state = state.rRuleEditorDialogState,
        onConfirm = {
            state.addRRule(it)
            state.hideRRuleDialog()
        },
    )
}

@Composable
private fun TopBar(
    routineProvider: () -> Routine?,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
) {
    TopAppBar(
        title = { routineProvider()?.let { Text(text = it.detail.title) } },
        modifier = modifier,
        navigationIcon = { NavigateUpButton(onClick = dropUnlessResumed(block = onNavigateUp)) },
    )
}
