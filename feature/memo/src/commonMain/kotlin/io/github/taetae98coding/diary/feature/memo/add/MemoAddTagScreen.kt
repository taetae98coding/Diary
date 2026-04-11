package io.github.taetae98coding.diary.feature.memo.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.feature.memo.common.MemoTagScaffold
import io.github.taetae98coding.diary.feature.memo.common.MemoTagStateHolder

@Composable
internal fun MemoAddTagScreen(
    navigateUp: () -> Unit,
    navigateToTagAdd: () -> Unit,
    stateHolder: MemoTagStateHolder,
    modifier: Modifier = Modifier,
) {
    MemoTagScaffold(
        stateHolder = stateHolder,
        modifier = modifier,
        onNavigateUp = navigateUp,
        onNavigateToTagAdd = navigateToTagAdd,
    )
}
