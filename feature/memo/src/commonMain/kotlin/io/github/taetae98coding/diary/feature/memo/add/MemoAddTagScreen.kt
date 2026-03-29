package io.github.taetae98coding.diary.feature.memo.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.presenter.memo.api.MemoAddStateHolder
import io.github.taetae98coding.diary.presenter.memo.compose.tag.MemoTagScaffold

@Composable
internal fun MemoAddTagScreen(
    navigateUp: () -> Unit,
    navigateToTagAdd: () -> Unit,
    stateHolder: MemoAddStateHolder,
    modifier: Modifier = Modifier,
) {
    MemoTagScaffold(
        stateHolder = stateHolder,
        modifier = modifier,
        onNavigateUp = navigateUp,
        onNavigateToTagAdd = navigateToTagAdd,
    )
}
