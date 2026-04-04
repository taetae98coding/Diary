package io.github.taetae98coding.diary.presenter.tag.compose.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.taetae98coding.diary.compose.core.button.AddFloatingButton
import io.github.taetae98coding.diary.compose.core.color.ColorCircle
import io.github.taetae98coding.diary.compose.core.icon.MemoIcon
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.presenter.tag.api.TagListStateHolder
import kotlin.uuid.Uuid

@Composable
public fun TagListScaffold(
    stateHolder: TagListStateHolder,
    modifier: Modifier = Modifier,
    state: TagListScaffoldState = rememberTagListScaffoldState(),
    isFetchingProvider: () -> Boolean = { false },
    onFetch: () -> Unit = {},
    onAdd: () -> Unit = {},
    onTag: (Uuid) -> Unit = {},
    onTagMemo: (Uuid) -> Unit = {},
) {
    val pagingItems = stateHolder.pagingData.collectAsLazyPagingItems()

    TagListScaffold(
        state = state,
        pagingItems = pagingItems,
        isFetchingProvider = isFetchingProvider,
        onFetch = onFetch,
        onAdd = onAdd,
        onTag = onTag,
        onTagMemo = onTagMemo,
        modifier = modifier,
    )
}

@Composable
internal fun TagListScaffold(
    state: TagListScaffoldState,
    pagingItems: LazyPagingItems<Tag>,
    isFetchingProvider: () -> Boolean = { false },
    onFetch: () -> Unit = {},
    onAdd: () -> Unit,
    onTag: (Uuid) -> Unit,
    onTagMemo: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
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
            modifier = Modifier
                .padding(paddingValues),
        ) {
            val isEmpty = !isFetchingProvider() && pagingItems.loadState.refresh !is LoadState.Loading && pagingItems.itemCount == 0

            Crossfade(targetState = isEmpty) { isEmpty ->
                if (isEmpty) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "태그가 없습니다",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = DiaryTheme.dimen.screenPaddingValues,
                        verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
                        horizontalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
                    ) {
                        if (isFetchingProvider() && pagingItems.itemCount == 0) {
                            items(count = 5) {
                                TagCard(
                                    uiState = null,
                                    onClick = {},
                                    onMemo = {},
                                )
                            }
                        } else {
                            items(
                                count = pagingItems.itemCount,
                                key = { pagingItems[it]?.id ?: it },
                            ) { index ->
                                val uiState = pagingItems[index]

                                TagCard(
                                    uiState = uiState,
                                    onClick = dropUnlessResumed { uiState?.id?.let(onTag) },
                                    onMemo = dropUnlessResumed { uiState?.id?.let(onTagMemo) },
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
        title = { Text(text = "태그") },
        modifier = modifier,
    )
}

@Composable
private fun TagCard(
    uiState: Tag?,
    onClick: () -> Unit,
    onMemo: () -> Unit,
    modifier: Modifier = Modifier,
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
                color = uiState?.detail?.color?.let(::Color) ?: Color.Unspecified,
                modifier = Modifier.size(8.dp),
            )
            Text(
                text = uiState?.detail?.title.orEmpty(),
                modifier = Modifier
                    .weight(1f)
                    .basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
            )
            IconButton(onClick = onMemo) {
                MemoIcon()
            }
        }
    }
}
