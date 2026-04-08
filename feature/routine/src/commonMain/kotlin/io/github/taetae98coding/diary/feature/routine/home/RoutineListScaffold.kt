package io.github.taetae98coding.diary.feature.routine.home

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import io.github.taetae98coding.diary.compose.core.button.AddFloatingButton
import io.github.taetae98coding.diary.compose.core.color.ColorCircle
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.routine.Routine
import kotlin.uuid.Uuid

@Composable
internal fun RoutineListScaffold(
    state: RoutineListScaffoldState,
    pagingItems: LazyPagingItems<Routine>,
    modifier: Modifier = Modifier,
    isFetchingProvider: () -> Boolean = { false },
    onAdd: () -> Unit = {},
    onRoutine: (Uuid) -> Unit = {},
) {
    Scaffold(
        modifier = modifier.focusableKeyEvent {
            if (it.type == KeyEventType.KeyDown && it.isMetaPressed && it.key == Key.A) {
                onAdd()
                true
            } else {
                false
            }
        },
        topBar = { TopBar() },
        floatingActionButton = { AddFloatingButton(onClick = dropUnlessResumed(block = onAdd)) },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = DiaryTheme.dimen.screenPaddingValues,
            verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
        ) {
            if (!isFetchingProvider() && pagingItems.loadState.refresh !is LoadState.Loading && pagingItems.itemCount == 0) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .animateItem(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "루틴이 없습니다",
                            style = DiaryTheme.typography.titleMedium,
                            color = DiaryTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            } else {
                items(
                    count = pagingItems.itemCount,
                    key = { pagingItems[it]?.id ?: it },
                ) { index ->
                    val routine = pagingItems[index]

                    if (routine != null) {
                        RoutineCard(
                            routine = routine,
                            onClick = dropUnlessResumed { onRoutine(routine.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = "루틴") },
        modifier = modifier,
    )
}

@Composable
private fun RoutineCard(
    routine: Routine,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ColorCircle(
                colorProvider = { Color(routine.detail.color) },
                modifier = Modifier.size(8.dp),
            )
            Text(
                text = routine.detail.title,
                modifier = Modifier
                    .weight(1f)
                    .basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1,
                style = DiaryTheme.typography.titleMedium,
            )
        }
    }
}
