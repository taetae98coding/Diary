package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.presenter.memo.api.MemoDetailStateHolder
import io.github.taetae98coding.diary.presenter.memo.compose.detail.MemoDetailScaffold
import kotlin.uuid.Uuid

@Composable
internal fun MemoDetailScreen(
    navigateUp: () -> Unit,
    navigateToMemoDetailTag: () -> Unit,
    navigateToTagDetail: (Uuid) -> Unit,
    stateHolder: MemoDetailStateHolder,
    modifier: Modifier = Modifier,
) {
    MemoDetailScaffold(
        stateHolder = stateHolder,
        modifier = modifier,
        onNavigateUp = navigateUp,
        onTagCard = navigateToMemoDetailTag,
        onTag = navigateToTagDetail,
    )
}
