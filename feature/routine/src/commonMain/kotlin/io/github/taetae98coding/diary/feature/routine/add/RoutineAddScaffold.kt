package io.github.taetae98coding.diary.feature.routine.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
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
import io.github.taetae98coding.diary.compose.core.card.LocalDateRangeCard
import io.github.taetae98coding.diary.compose.core.card.TitleCard
import io.github.taetae98coding.diary.compose.core.icon.AddIcon
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.padding.plus
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.scaffold.DiaryScaffold
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.routine.add.component.CalendarVisibilityCard
import io.github.taetae98coding.diary.feature.routine.add.component.RRuleEditorDialog
import io.github.taetae98coding.diary.feature.routine.add.component.RRuleSummaryCard
import io.github.taetae98coding.diary.feature.routine.add.component.RoutineCountEditor

@Composable
internal fun RoutineAddScaffold(
    state: RoutineAddScaffoldState,
    modifier: Modifier = Modifier,
    isInProgressProvider: () -> Boolean = { false },
    onNavigateUp: () -> Unit = {},
    onAdd: () -> Unit = {},
) {
    DiaryScaffold(
        modifier = modifier
            .focusableKeyEvent(autoFocus = false) { event ->
                if (event.type == KeyEventType.KeyDown && event.isMetaPressed && event.key == Key.Enter) {
                    onAdd()
                    true
                } else {
                    false
                }
            }
            .imePadding(),
        topBar = { TopBar(onNavigateUp = onNavigateUp) },
        floatingActionButton = {
            AddFloatingButton(
                onClick = dropUnlessResumed(block = onAdd),
                isInProgressProvider = isInProgressProvider,
            )
        },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
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
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
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
