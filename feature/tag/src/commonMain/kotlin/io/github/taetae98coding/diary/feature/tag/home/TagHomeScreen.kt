package io.github.taetae98coding.diary.feature.tag.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import kotlin.uuid.Uuid
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun TagHomeScreen(
    navigateToTagAdd: () -> Unit,
    navigateToTagDetail: (Uuid) -> Unit,
    navigateToTagMemo: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: TagHomeViewModel = koinViewModel(),
    listViewModel: TagListViewModel = koinViewModel(),
) {
    val isSyncing by homeViewModel.isSyncing.collectAsStateWithLifecycle()
    val pagingItems = listViewModel.pagingData.collectAsLazyPagingItems()
    val state = rememberTagListScaffoldState()

    SyncFailedEffect(viewModel = homeViewModel, state = state)

    TagListScaffold(
        state = state,
        pagingItems = pagingItems,
        isFetchingProvider = { isSyncing },
        onFetch = homeViewModel::sync,
        onAdd = navigateToTagAdd,
        onTag = navigateToTagDetail,
        onTagMemo = navigateToTagMemo,
        modifier = modifier,
    )
}

@Composable
private fun SyncFailedEffect(
    viewModel: TagHomeViewModel,
    state: TagListScaffoldState,
) {
    val isFailed by viewModel.isFailed.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isFailed) {
        if (isFailed) {
            coroutineScope.launch { state.hostState.showImmediate("오프라인입니다.") }
        }
    }
}
