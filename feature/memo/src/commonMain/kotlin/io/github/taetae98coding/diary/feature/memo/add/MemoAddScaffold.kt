package io.github.taetae98coding.diary.feature.memo.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import io.github.taetae98coding.diary.compose.core.card.DateRangeCard
import io.github.taetae98coding.diary.compose.core.card.DescriptionCard
import io.github.taetae98coding.diary.compose.core.card.TitleCard
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.padding.plus
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.scaffold.DiaryScaffold
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.memo.common.TagCard
import io.github.taetae98coding.diary.feature.memo.common.TagCardUiState
import kotlin.uuid.Uuid

@Composable
internal fun MemoAddScaffold(
    modifier: Modifier = Modifier,
    state: MemoAddScaffoldState = rememberMemoAddScaffoldState(),
    tagCardUiStateProvider: () -> TagCardUiState = { TagCardUiState.Loading },
    isInProgressProvider: () -> Boolean = { false },
    onNavigateUp: () -> Unit = {},
    onTagCard: () -> Unit = {},
    onTag: (Uuid) -> Unit = {},
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
            }.imePadding(),
        topBar = { TopBar(onNavigateUp = onNavigateUp) },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
        floatingActionButton = {
            AddFloatingButton(
                onClick = dropUnlessResumed(block = onAdd),
                isInProgressProvider = isInProgressProvider,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
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
                DateRangeCard(state = state.dateRangeCardState)
            }

            item {
                ColorCard(
                    state = state.colorCardState,
                    modifier = Modifier.fillParentMaxWidth(),
                )
            }

            item {
                TagCard(
                    uiStateProvider = tagCardUiStateProvider,
                    onClick = onTagCard,
                    onTag = onTag,
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
        title = { Text(text = "메모 추가") },
        modifier = modifier,
        navigationIcon = { NavigateUpButton(onClick = dropUnlessResumed(block = onNavigateUp)) },
    )
}

@ScreenPreview
@Composable
private fun Preview() {
    DiaryTheme {
        MemoAddScaffold()
    }
}

@ScreenPreview
@Composable
private fun InProgressPreview() {
    DiaryTheme {
        MemoAddScaffold(isInProgressProvider = { true })
    }
}
