package io.github.taetae98coding.diary.feature.buddy.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.compose.memo.MemoListDetailPaneScaffold
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldUiState
import io.github.taetae98coding.diary.core.compose.memo.detail.rememberMemoDetailScaffoldDetailState
import io.github.taetae98coding.diary.core.compose.topbar.TopBarTitle
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun BuddyGroupMemoDetailRoute(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    detailViewModel: BuddyGroupMemoDetailViewModel = koinViewModel(),
) {
    val state = rememberMemoDetailScaffoldDetailState(
        onDelete = { },
        onUpdate = { },
        detailProvider = { null },
    )

    MemoListDetailPaneScaffold(
        onNavigateUp = navigateUp,
        onDetailTag = { },
        detailScaffoldStateProvider = { state },
        detailUiStateProvider = { MemoDetailScaffoldUiState() },
        detailTagListProvider = { null },
        detailTitle = { TopBarTitle(text = "") },
        onTagAdd = {},
        tagListProvider = { null },
        modifier = modifier,
    )
}
