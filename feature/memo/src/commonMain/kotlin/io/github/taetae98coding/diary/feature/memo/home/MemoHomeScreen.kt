package io.github.taetae98coding.diary.feature.memo.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import io.github.taetae98coding.diary.presenter.memo.api.ListMemoFilterStateHolder
import io.github.taetae98coding.diary.presenter.memo.api.MemoListStateHolder
import io.github.taetae98coding.diary.presenter.memo.compose.list.MemoListScaffold
import io.github.taetae98coding.diary.presenter.memo.compose.list.MemoListScaffoldState
import io.github.taetae98coding.diary.presenter.memo.compose.list.rememberMemoListScaffoldState
import kotlin.uuid.Uuid
import kotlinx.coroutines.launch
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
    val state = rememberMemoListScaffoldState()

    SyncFailedEffect(viewModel = viewModel, state = state)

    MemoListScaffold(
        memoListStateHolder = memoListStateHolder,
        filterStateHolder = filterStateHolder,
        modifier = modifier,
        state = state,
        isFetchingProvider = { isSyncing },
        onFetch = viewModel::sync,
        onFilterClick = navigateToFilter,
        onNavigateToAdd = navigateToMemoAdd,
        onNavigateToDetail = navigateToMemoDetail,
    )
}

@Composable
private fun SyncFailedEffect(
    viewModel: MemoHomeViewModel,
    state: MemoListScaffoldState,
) {
    val isFailed by viewModel.isFailed.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isFailed) {
        if (isFailed) {
            coroutineScope.launch { state.hostState.showImmediate("오프라인입니다.") }
        }
    }
}
