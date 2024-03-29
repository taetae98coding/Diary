package com.taetae98.diary.feature.memo.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.taetae98.diary.feature.memo.detail.MemoDetailScreen
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
@NonRestartableComposable
internal fun MemoAddRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    viewModel: MemoAddViewModel,
) {
    MemoDetailScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        uiState = viewModel.uiState,
        message = viewModel.message.collectAsStateOnLifecycle(),
        toolbarMessage = remember { MutableStateFlow(null) }.collectAsStateOnLifecycle(),
        toolbarUiState = viewModel.toolbarUiState.collectAsStateOnLifecycle(),
        titleUiState = viewModel.titleUiStateHolder.uiState.collectAsStateOnLifecycle(),
        descriptionUiState = viewModel.descriptionUiStateHolder.uiState.collectAsStateOnLifecycle(),
        dateRangeUiState = viewModel.dateRangeUiStateHolder.uiState.collectAsStateOnLifecycle(),
        tagUiState = viewModel.tagUiState.collectAsLazyPagingItems(),
    )
}
