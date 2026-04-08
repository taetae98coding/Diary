package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.feature.memo.common.MemoTagScaffold
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MemoDetailTagScreen(
    navigateUp: () -> Unit,
    navigateToTagAdd: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MemoDetailTagViewModel = koinViewModel(),
) {
    MemoTagScaffold(
        stateHolder = viewModel,
        modifier = modifier,
        onNavigateUp = navigateUp,
        onNavigateToTagAdd = navigateToTagAdd,
    )
}
