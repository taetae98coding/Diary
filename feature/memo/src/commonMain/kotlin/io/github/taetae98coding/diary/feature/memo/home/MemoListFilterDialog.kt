package io.github.taetae98coding.diary.feature.memo.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlexAlignContent
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexJustifyContent
import androidx.compose.foundation.layout.FlexWrap
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.taetae98coding.diary.compose.core.chip.DiaryFilterChip
import io.github.taetae98coding.diary.compose.core.color.ColorCircle
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.library.compose.ui.wcagAAAContentColor
import kotlin.uuid.Uuid
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MemoListFilterDialog(
    navigateUp: () -> Unit,
    navigateToTagAdd: () -> Unit,
    viewModel: MemoListFilterViewModel = koinViewModel(),
) {
    val pagingItems = viewModel.filterTagPagingData.collectAsLazyPagingItems()
    val filterOption by viewModel.filterOption.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = navigateUp,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MemoTagFilterLayout(
                pagingItems = pagingItems,
                onTagAdd = navigateToTagAdd,
                onSelect = viewModel::select,
                onUnselect = viewModel::unselect,
            )
            HasMemoTagFilterLayout(
                presenceProvider = { filterOption.tagPresence },
                onClick = viewModel::setTagPresence,
            )
            DateRangeFilterLayout(
                presenceProvider = { filterOption.datePresence },
                onClick = viewModel::setDatePresence,
            )
        }
    }
}

@Composable
private fun MemoTagFilterLayout(
    pagingItems: LazyPagingItems<MemoListFilterTagUiState>,
    onTagAdd: () -> Unit,
    onSelect: (Uuid) -> Unit,
    onUnselect: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "메모태그",
            style = DiaryTheme.typography.titleMedium,
        )
        FlexBox(
            modifier = Modifier.fillMaxWidth(),
            config = {
                wrap(FlexWrap.Wrap)
                gap(6.dp)
                justifyContent(FlexJustifyContent.Center)
                alignContent(FlexAlignContent.Center)
            },
        ) {
            if (pagingItems.loadState.refresh !is LoadState.Loading && pagingItems.itemCount == 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "태그가 없습니다",
                        style = DiaryTheme.typography.bodyMedium,
                        color = DiaryTheme.colorScheme.onSurfaceVariant,
                    )
                    Button(onClick = dropUnlessResumed(block = onTagAdd)) {
                        Text(text = "추가하기")
                    }
                }
            } else {
                repeat(pagingItems.itemCount) { index ->
                    val uiState = pagingItems[index]
                    val color = uiState?.color?.let(::Color) ?: Color.Unspecified

                    DiaryFilterChip(
                        selected = uiState?.isSelected ?: false,
                        onClick = {
                            uiState?.let {
                                if (it.isSelected) {
                                    onUnselect(it.id)
                                } else {
                                    onSelect(it.id)
                                }
                            }
                        },
                        label = { Text(text = uiState?.title.orEmpty()) },
                        modifier = if (uiState == null) Modifier.width(80.dp) else Modifier,
                        leadingIcon = {
                            ColorCircle(
                                colorProvider = { color },
                                modifier = Modifier.size(8.dp),
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = color,
                            selectedLabelColor = color.wcagAAAContentColor(),
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun HasMemoTagFilterLayout(
    presenceProvider: () -> FilterPresence,
    onClick: (FilterPresence) -> Unit,
    modifier: Modifier = Modifier,
) {
    val presence = presenceProvider()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "메모태그 유무",
            style = DiaryTheme.typography.titleMedium,
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            listOf(
                FilterPresence.YES,
                FilterPresence.NO,
                FilterPresence.NONE,
            ).forEachIndexed { index, value ->
                SegmentedButton(
                    selected = value == presence,
                    onClick = { onClick(value) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 3),
                ) {
                    when (value) {
                        FilterPresence.YES -> Text(text = "태그O")
                        FilterPresence.NO -> Text(text = "태그X")
                        FilterPresence.NONE -> Text(text = "X")
                    }
                }
            }
        }
    }
}

@Composable
private fun DateRangeFilterLayout(
    presenceProvider: () -> FilterPresence,
    onClick: (FilterPresence) -> Unit,
    modifier: Modifier = Modifier,
) {
    val presence = presenceProvider()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "날짜 유무",
            style = DiaryTheme.typography.titleMedium,
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            listOf(
                FilterPresence.YES,
                FilterPresence.NO,
                FilterPresence.NONE,
            ).forEachIndexed { index, value ->
                SegmentedButton(
                    selected = value == presence,
                    onClick = { onClick(value) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 3),
                ) {
                    when (value) {
                        FilterPresence.YES -> Text(text = "날짜O")
                        FilterPresence.NO -> Text(text = "날짜X")
                        FilterPresence.NONE -> Text(text = "X")
                    }
                }
            }
        }
    }
}
