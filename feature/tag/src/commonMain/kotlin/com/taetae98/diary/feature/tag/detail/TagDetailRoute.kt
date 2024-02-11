package com.taetae98.diary.feature.tag.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
internal fun TagDetailRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    viewModel: TagDetailViewModel,
) {
    val message by viewModel.message.collectAsStateOnLifecycle()

    TagDetailScreen(
        modifier = modifier,
        onNavigateUp = viewModel::upsert,
        titleUiState = viewModel.titleUiStateHolder.uiState.collectAsStateOnLifecycle(),
        descriptionUiState = viewModel.descriptionUiStateHolder.uiState.collectAsStateOnLifecycle(),
    )

    LaunchedEffect(message) {
        if (message == TagDetailMessage.Upsert) {
            onNavigateUp()
        }
    }
}
