package io.github.taetae98coding.diary.feature.tag.memo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.presenter.memo.api.MemoListStateHolder
import io.github.taetae98coding.diary.presenter.memo.compose.list.MemoListNavigation
import io.github.taetae98coding.diary.presenter.memo.compose.list.MemoListScaffold
import kotlin.uuid.Uuid
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun TagMemoScreen(
    navigateUp: () -> Unit,
    navigateToMemoAdd: () -> Unit,
    navigateToMemoDetail: (Uuid) -> Unit,
    stateHolder: MemoListStateHolder,
    modifier: Modifier = Modifier,
    viewModel: TagMemoViewModel = koinViewModel(),
) {
    val title by viewModel.title.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    MemoListScaffold(
        memoListStateHolder = stateHolder,
        modifier = modifier,
        titleProvider = { title },
        navigationProvider = { MemoListNavigation.NavigateUp(onNavigateUp = navigateUp) },
        isFetchingProvider = { isSyncing },
        onFetch = viewModel::sync,
        onNavigateToAdd = navigateToMemoAdd,
        onNavigateToDetail = navigateToMemoDetail,
    )
}
