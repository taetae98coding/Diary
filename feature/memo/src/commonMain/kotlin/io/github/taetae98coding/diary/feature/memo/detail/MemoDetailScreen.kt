package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.presenter.memo.api.MemoDetailStateHolder
import io.github.taetae98coding.diary.presenter.memo.compose.detail.MemoDetailScaffold

@Composable
internal fun MemoDetailScreen(
    stateHolder: MemoDetailStateHolder,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
) {
    MemoDetailScaffold(
        stateHolder = stateHolder,
        modifier = modifier,
        onNavigateUp = navigateUp,
    )
}
