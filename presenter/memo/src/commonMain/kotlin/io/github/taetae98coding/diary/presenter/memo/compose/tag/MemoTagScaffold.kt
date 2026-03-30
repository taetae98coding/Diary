package io.github.taetae98coding.diary.presenter.memo.compose.tag

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.taetae98coding.diary.compose.core.button.NavigateUpButton
import io.github.taetae98coding.diary.compose.core.chip.DiaryFilterChip
import io.github.taetae98coding.diary.compose.core.color.ColorCircle
import io.github.taetae98coding.diary.compose.core.icon.AddIcon
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.library.compose.ui.wcagAAAContentColor
import io.github.taetae98coding.diary.presenter.memo.api.MemoTagStateHolder
import io.github.taetae98coding.diary.presenter.memo.api.MemoTagUiState
import kotlin.uuid.Uuid

@Composable
public fun MemoTagScaffold(
    stateHolder: MemoTagStateHolder,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onNavigateToTagAdd: () -> Unit = {},
) {
    val primaryTagPagingItems = stateHolder.primaryPagingData.collectAsLazyPagingItems()
    val memoTagPagingItems = stateHolder.memoTagPagingData.collectAsLazyPagingItems()

    MemoTagScaffold(
        primaryTagPagingItems = primaryTagPagingItems,
        memoTagPagingItems = memoTagPagingItems,
        onSelectPrimaryTag = stateHolder::selectPrimaryTag,
        onUnselectPrimaryTag = stateHolder::unselectPrimaryTag,
        onSelectMemoTag = stateHolder::selectMemoTag,
        onUnselectMemoTag = stateHolder::unselectMemoTag,
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        onNavigateToTagAdd = onNavigateToTagAdd,
    )
}

@Composable
private fun MemoTagScaffold(
    primaryTagPagingItems: LazyPagingItems<MemoTagUiState>,
    memoTagPagingItems: LazyPagingItems<MemoTagUiState>,
    onSelectPrimaryTag: (Uuid) -> Unit,
    onUnselectPrimaryTag: (Uuid) -> Unit,
    onSelectMemoTag: (Uuid) -> Unit,
    onUnselectMemoTag: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onNavigateToTagAdd: () -> Unit = {},
) {
    Scaffold(
        topBar = { TopBar(onNavigateUp = onNavigateUp) },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
                .fillMaxSize()
                .padding(DiaryTheme.dimen.screenPaddingValues),
            verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.screenCardSpace),
        ) {
            TagCard(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .minimumInteractiveComponentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "대표 태그",
                            style = DiaryTheme.typography.titleMedium,
                        )
                    }
                },
                pagingItems = primaryTagPagingItems,
                onSelect = onSelectPrimaryTag,
                onUnselect = onUnselectPrimaryTag,
                modifier = Modifier.weight(1F),
            )
            TagCard(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable(onClick = dropUnlessResumed(block = onNavigateToTagAdd))
                            .padding(horizontal = 16.dp)
                            .minimumInteractiveComponentSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "메모 태그",
                            style = DiaryTheme.typography.titleMedium,
                        )
                        AddIcon()
                    }
                },
                pagingItems = memoTagPagingItems,
                onSelect = onSelectMemoTag,
                onUnselect = onUnselectMemoTag,
                modifier = Modifier.weight(1F),
            )
        }
    }
}

@Composable
private fun TopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = "메모 태그") },
        modifier = modifier,
        navigationIcon = { NavigateUpButton(onClick = dropUnlessResumed(block = onNavigateUp)) },
    )
}

@Composable
private fun TagCard(
    title: @Composable () -> Unit,
    pagingItems: LazyPagingItems<MemoTagUiState>,
    onSelect: (Uuid) -> Unit,
    onUnselect: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        title()
        FlowRow(
            modifier = Modifier.fillMaxWidth()
                .weight(1F)
                .verticalScroll(rememberScrollState())
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        ) {
            if (pagingItems.loadState.refresh !is LoadState.Loading && pagingItems.itemCount == 0) {
                Text(
                    text = "태그가 없습니다",
                    style = DiaryTheme.typography.bodyMedium,
                )
            } else {
                repeat(pagingItems.itemCount) { index ->
                    val uiState = pagingItems[index]

                    TagChip(
                        uiState = uiState,
                        onSelect = { uiState?.let { onSelect(it.tagId) } },
                        onUnselect = { uiState?.let { onUnselect(it.tagId) } },
                        modifier = if (uiState == null) {
                            Modifier.width(80.dp)
                        } else {
                            Modifier
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun TagChip(
    uiState: MemoTagUiState?,
    onSelect: () -> Unit,
    onUnselect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = uiState?.color?.let(::Color) ?: Color.Unspecified

    DiaryFilterChip(
        selected = uiState?.isSelected ?: false,
        onClick = {
            uiState?.let {
                if (it.isSelected) {
                    onUnselect()
                } else {
                    onSelect()
                }
            }
        },
        label = { Text(text = uiState?.title.orEmpty()) },
        modifier = modifier,
        leadingIcon = {
            ColorCircle(
                color = color,
                modifier = Modifier.size(8.dp),
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color,
            selectedLabelColor = color.wcagAAAContentColor(),
        ),
    )
}
