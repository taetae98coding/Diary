package com.taetae98.diary.feature.tag.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
internal fun TagAddRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    viewModel: TagAddViewModel,
) {
    TagAddScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        titleUiState = viewModel.titleUiStateHolder.uiState.collectAsStateOnLifecycle(),
        onAdd = {},
    )
}
