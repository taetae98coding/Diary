package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.presenter.memo.api.MemoDetailTagStateHolder
import io.github.taetae98coding.diary.presenter.memo.compose.tag.MemoTagScaffold

@Composable
internal fun MemoDetailTagScreen(
    navigateUp: () -> Unit,
    navigateToTagAdd: () -> Unit,
    stateHolder: MemoDetailTagStateHolder,
    modifier: Modifier = Modifier,
) {
    MemoTagScaffold(
        stateHolder = stateHolder,
        modifier = modifier,
        onNavigateUp = navigateUp,
        onNavigateToTagAdd = navigateToTagAdd,
    )
}
