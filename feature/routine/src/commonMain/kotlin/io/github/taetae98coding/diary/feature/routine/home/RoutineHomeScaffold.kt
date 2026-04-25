package io.github.taetae98coding.diary.feature.routine.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.taetae98coding.diary.compose.core.button.AddFloatingButton
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.routine.Routine
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun RoutineHomeScaffold(
    pagingItems: LazyPagingItems<Routine>,
    modifier: Modifier = Modifier,
    state: RoutineHomeScaffoldState = rememberRoutineHomeScaffoldState(),
    isFetchingProvider: () -> Boolean = { false },
    onFetch: () -> Unit = {},
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
        PullToRefreshBox(
            isRefreshing = isFetchingProvider(),
            onRefresh = onFetch,
            modifier = Modifier.padding(paddingValues),
        ) {
            val isEmpty by remember {
                derivedStateOf {
                    !isFetchingProvider() && pagingItems.loadState.refresh !is LoadState.Loading && pagingItems.itemCount == 0
                }
            }

            Crossfade(targetState = isEmpty) { isEmpty ->
                if (isEmpty) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "루틴이 없습니다",
                            style = DiaryTheme.typography.titleMedium,
                            color = DiaryTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = DiaryTheme.dimen.screenPaddingValues,
                        verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
                    ) {
                        if (isFetchingProvider() && pagingItems.itemCount == 0) {
                            items(count = 5) {
                                RoutineCard(uiState = null)
                            }
                        } else {
                            items(
                                count = pagingItems.itemCount,
                                key = { pagingItems[it]?.id ?: it },
                            ) { index ->
                                val uiState = pagingItems[index]

                                RoutineCard(
                                    uiState = uiState,
                                    onClick = dropUnlessResumed { uiState?.id?.let(onRoutine) },
                                    modifier = Modifier.animateItem(),
                                )
                            }
                        }
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

@ScreenPreview
@Composable
private fun Preview() {
    DiaryTheme {
        RoutineHomeScaffold(
            pagingItems = flowOf(PagingData.empty<Routine>()).collectAsLazyPagingItems(),
        )
    }
}
