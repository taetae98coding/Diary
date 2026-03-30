package io.github.taetae98coding.diary.feature.memo.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.presenter.memo.api.ListMemoFilterStateHolder
import io.github.taetae98coding.diary.presenter.memo.api.MemoListStateHolder
import io.github.taetae98coding.diary.presenter.memo.compose.list.MemoListScaffold
import kotlin.uuid.Uuid
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MemoHomeScreen(
    navigateToMemoAdd: () -> Unit,
    navigateToMemoDetail: (Uuid) -> Unit,
    navigateToFilter: () -> Unit,
    memoListStateHolder: MemoListStateHolder,
    filterStateHolder: ListMemoFilterStateHolder,
    modifier: Modifier = Modifier,
    viewModel: MemoHomeViewModel = koinViewModel(),
) {
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    MemoListScaffold(
        memoListStateHolder = memoListStateHolder,
        filterStateHolder = filterStateHolder,
        modifier = modifier,
        isFetchingProvider = { isSyncing },
        onFetch = viewModel::sync,
        onFilterClick = navigateToFilter,
        onNavigateToAdd = navigateToMemoAdd,
        onNavigateToDetail = navigateToMemoDetail,
    )
}
