package io.github.taetae98coding.diary.presenter.memo.compose.list

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.taetae98coding.diary.compose.core.button.AddFloatingButton
import io.github.taetae98coding.diary.compose.core.button.NavigateUpButton
import io.github.taetae98coding.diary.compose.core.color.ColorCircle
import io.github.taetae98coding.diary.compose.core.icon.FilterListIcon
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import io.github.taetae98coding.diary.compose.core.swipe.SwipeFinishAndDismissBox
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.presenter.memo.api.ListMemoFilterStateHolder
import io.github.taetae98coding.diary.presenter.memo.api.MemoListEffect
import io.github.taetae98coding.diary.presenter.memo.api.MemoListStateHolder
import kotlin.uuid.Uuid

@Composable
public fun MemoListScaffold(
    memoListStateHolder: MemoListStateHolder,
    filterStateHolder: ListMemoFilterStateHolder,
    modifier: Modifier = Modifier,
    state: MemoListScaffoldState = rememberMemoListScaffoldState(),
    titleProvider: () -> String = { "메모" },
    navigationProvider: () -> MemoListNavigation = { MemoListNavigation.None },
    isFetchingProvider: () -> Boolean = { false },
    onFetch: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    onNavigateToDetail: (Uuid) -> Unit = {},
) {
    val pagingItems = memoListStateHolder.pagingData.collectAsLazyPagingItems()
    val hasFilter by filterStateHolder.hasFilter.collectAsStateWithLifecycle()

    MemoListScaffold(
        state = state,
        pagingItems = pagingItems,
        titleProvider = titleProvider,
        navigationProvider = navigationProvider,
        actions = {
            IconButton(
                onClick = dropUnlessResumed(block = onFilterClick),
                colors = if (hasFilter) {
                    IconButtonDefaults.iconButtonColors(contentColor = DiaryTheme.colorScheme.primary)
                } else {
                    IconButtonDefaults.iconButtonColors()
                },
            ) {
                FilterListIcon()
            }
        },
        isFetchingProvider = isFetchingProvider,
        onFetch = onFetch,
        onFinish = memoListStateHolder::finish,
        onDelete = memoListStateHolder::delete,
        onNavigateToAdd = onNavigateToAdd,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier,
    )

    MemoListEffectHandler(
        stateHolder = memoListStateHolder,
        state = state,
    )
}

@Composable
public fun MemoListScaffold(
    memoListStateHolder: MemoListStateHolder,
    modifier: Modifier = Modifier,
    state: MemoListScaffoldState = rememberMemoListScaffoldState(),
    titleProvider: () -> String = { "메모" },
    navigationProvider: () -> MemoListNavigation = { MemoListNavigation.None },
    isFetchingProvider: () -> Boolean = { false },
    onFetch: () -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    onNavigateToDetail: (Uuid) -> Unit = {},
) {
    val pagingItems = memoListStateHolder.pagingData.collectAsLazyPagingItems()

    MemoListScaffold(
        state = state,
        pagingItems = pagingItems,
        titleProvider = titleProvider,
        navigationProvider = navigationProvider,
        isFetchingProvider = isFetchingProvider,
        onFetch = onFetch,
        onFinish = memoListStateHolder::finish,
        onDelete = memoListStateHolder::delete,
        onNavigateToAdd = onNavigateToAdd,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier,
    )

    MemoListEffectHandler(
        stateHolder = memoListStateHolder,
        state = state,
    )
}

@Composable
private fun MemoListScaffold(
    state: MemoListScaffoldState,
    pagingItems: LazyPagingItems<Memo>,
    modifier: Modifier = Modifier,
    titleProvider: () -> String = { "메모" },
    navigationProvider: () -> MemoListNavigation = { MemoListNavigation.None },
    actions: @Composable RowScope.() -> Unit = {},
    isFetchingProvider: () -> Boolean = { false },
    onFetch: () -> Unit = {},
    onFinish: (Uuid) -> Unit = {},
    onDelete: (Uuid) -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    onNavigateToDetail: (Uuid) -> Unit = {},
) {
    Scaffold(
        modifier = modifier.focusableKeyEvent {
            if (it.type == KeyEventType.KeyDown && it.isMetaPressed && it.key == Key.A) {
                onNavigateToAdd()
                true
            } else {
                false
            }
        },
        topBar = {
            TopBar(
                titleProvider = titleProvider,
                navigationProvider = navigationProvider,
                actions = actions,
            )
        },
        floatingActionButton = { AddFloatingButton(onClick = dropUnlessResumed(block = onNavigateToAdd)) },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isFetchingProvider(),
            onRefresh = onFetch,
            modifier = Modifier
                .padding(paddingValues),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
                                text = "메모가 없습니다",
                                style = DiaryTheme.typography.titleMedium,
                                color = DiaryTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                } else if (isFetchingProvider() && pagingItems.itemCount == 0) {
                    items(count = 5) {
                        MemoCard(
                            uiState = null,
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                        )
                    }
                } else {
                    items(
                        count = pagingItems.itemCount,
                        key = { pagingItems[it]?.id ?: it },
                    ) { index ->
                        val uiState = pagingItems[index]

                        SwipeFinishAndDismissBox(
                            onFinish = { uiState?.id?.let(onFinish) },
                            onDelete = { uiState?.id?.let(onDelete) },
                            modifier = Modifier.animateItem(),
                        ) {
                            MemoCard(
                                uiState = uiState,
                                onClick = dropUnlessResumed { uiState?.id?.let(onNavigateToDetail) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemoListEffectHandler(
    stateHolder: MemoListStateHolder,
    state: MemoListScaffoldState,
) {
    val effect by stateHolder.effect.collectAsStateWithLifecycle()

    LaunchedEffect(stateHolder, state, effect) {
        when (val current = effect) {
            is MemoListEffect.FinishComplete -> {
                val result = state.hostState.showImmediate(
                    message = "메모 완료",
                    actionLabel = "취소",
                    duration = SnackbarDuration.Short,
                )
                if (result == SnackbarResult.ActionPerformed) {
                    stateHolder.restart(current.memoId)
                }
                stateHolder.consumeEffect()
            }

            is MemoListEffect.DeleteComplete -> {
                val result = state.hostState.showImmediate(
                    message = "메모 삭제",
                    actionLabel = "취소",
                    duration = SnackbarDuration.Short,
                )
                if (result == SnackbarResult.ActionPerformed) {
                    stateHolder.restore(current.memoId)
                }
                stateHolder.consumeEffect()
            }

            MemoListEffect.None -> Unit
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    titleProvider: () -> String = { "" },
    navigationProvider: () -> MemoListNavigation = { MemoListNavigation.None },
    actions: @Composable RowScope.() -> Unit = {},
) {
    val navigation = navigationProvider()

    TopAppBar(
        title = { Text(text = titleProvider()) },
        modifier = modifier,
        navigationIcon = {
            when (navigation) {
                is MemoListNavigation.None -> Unit

                is MemoListNavigation.NavigateUp -> NavigateUpButton(
                    onClick = dropUnlessResumed(block = navigation.onNavigateUp),
                )
            }
        },
        actions = actions,
    )
}

@Composable
private fun MemoCard(
    modifier: Modifier = Modifier,
    uiState: Memo? = null,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ColorCircle(
                colorProvider = { uiState?.detail?.color?.let(::Color) ?: Color.Unspecified },
                modifier = Modifier.size(8.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = uiState?.detail?.title.orEmpty(),
                    modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                    maxLines = 1,
                    style = DiaryTheme.typography.titleMedium,
                )
                uiState?.detail?.localDateTimeRange?.let {
                    val text = listOfNotNull(it.start.date, it.endInclusive.date)
                        .distinct()
                        .joinToString(separator = " ~ ")

                    Text(
                        text = text,
                        modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                        maxLines = 1,
                        style = DiaryTheme.typography.bodySmall,
                        color = DiaryTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
