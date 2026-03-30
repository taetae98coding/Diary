package io.github.taetae98coding.diary.feature.calendar.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.taetae98coding.diary.compose.core.chip.DiaryFilterChip
import io.github.taetae98coding.diary.compose.core.color.ColorCircle
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.library.compose.ui.wcagAAAContentColor
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CalendarFilterDialog(
    navigateUp: () -> Unit,
    navigateToTagAdd: () -> Unit,
    viewModel: CalendarFilterViewModel = koinViewModel(),
) {
    val pagingItems = viewModel.filterTagPagingData.collectAsLazyPagingItems()

    ModalBottomSheet(
        onDismissRequest = navigateUp,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "메모태그",
                style = DiaryTheme.typography.titleMedium,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            ) {
                if (pagingItems.loadState.refresh !is LoadState.Loading && pagingItems.itemCount == 0) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "태그가 없습니다",
                            style = DiaryTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Button(onClick = dropUnlessResumed(block = navigateToTagAdd)) {
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
                                        viewModel.unselect(it.id)
                                    } else {
                                        viewModel.select(it.id)
                                    }
                                }
                            },
                            label = { Text(text = uiState?.title.orEmpty()) },
                            modifier = if (uiState == null) Modifier.width(80.dp) else Modifier,
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
                }
            }
        }
    }
}
