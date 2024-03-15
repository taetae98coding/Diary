package com.taetae98.diary.feature.tag.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
@NonRestartableComposable
internal fun TagDetailRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    viewModel: TagDetailViewModel,
    toolbarViewModel: TagDetailToolbarViewModel,
) {
    TagDetailScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        uiState = viewModel.uiState,
        message = viewModel.message.collectAsStateOnLifecycle(),
        toolbarUiState = toolbarViewModel.uiState,
        toolbarMessage = toolbarViewModel.message.collectAsStateOnLifecycle(),
        titleUiState = viewModel.titleUiStateHolder.uiState.collectAsStateOnLifecycle(),
        descriptionUiState = viewModel.descriptionUiStateHolder.uiState.collectAsStateOnLifecycle(),
    )
}
