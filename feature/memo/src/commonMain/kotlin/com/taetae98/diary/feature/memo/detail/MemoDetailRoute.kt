package com.taetae98.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.runtime.collectAsStateOnLifecycle

@Composable
internal fun MemoDetailRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    memoDetailViewModel: MemoDetailViewModel,
) {
    val uiState = memoDetailViewModel.uiState.collectAsStateOnLifecycle()

    MemoDetailScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        uiState = uiState,
        toolbarUiState = memoDetailViewModel.toolbarUiState.collectAsStateOnLifecycle(),
        titleUiState = memoDetailViewModel.titleUiState.collectAsStateOnLifecycle(),
    )

    LaunchedEffect(uiState.value.message) {
        when (uiState.value.message) {
            MemoDetailMessage.Update, MemoDetailMessage.Delete -> onNavigateUp()
            else -> Unit
        }
    }
}
