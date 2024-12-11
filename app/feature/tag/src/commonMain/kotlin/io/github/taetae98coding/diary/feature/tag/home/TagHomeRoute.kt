package io.github.taetae98coding.diary.feature.tag.home

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.tag.TagListMultiPaneScaffold
import io.github.taetae98coding.diary.feature.tag.add.TagAddViewModel
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailViewModel
import io.github.taetae98coding.diary.feature.tag.list.TagListViewModel
import io.github.taetae98coding.diary.feature.tag.memo.TagMemoViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun TagHomeRoute(
    navigateToMemoAdd: (String) -> Unit,
    navigateToMemoDetail: (String) -> Unit,
    onScaffoldValueChange: (ThreePaneScaffoldValue) -> Unit,
    modifier: Modifier = Modifier,
    listViewModel: TagListViewModel = koinViewModel(),
    addViewModel: TagAddViewModel = koinViewModel(),
    detailViewModel: TagDetailViewModel = koinViewModel(),
    memoViewModel: TagMemoViewModel = koinViewModel(),
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<String?>()
    val listScreenUiState by listViewModel.uiState.collectAsStateWithLifecycle()
    val listUiState by listViewModel.list.collectAsStateWithLifecycle()
    val addUiState by addViewModel.uiState.collectAsStateWithLifecycle()
    val tagDetail by detailViewModel.tagDetail.collectAsStateWithLifecycle()
    val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()
    val detailActionsUiState by detailViewModel.actionsUiState.collectAsStateWithLifecycle()
    val tagMemoListUiState by memoViewModel.memoList.collectAsStateWithLifecycle()
    val tagMemoUiState by memoViewModel.uiState.collectAsStateWithLifecycle()

    TagListMultiPaneScaffold(
        navigateToMemoAdd = navigateToMemoAdd,
        navigateToMemoDetail = navigateToMemoDetail,
        navigator = navigator,
        listScaffoldUiStateProvider = { listScreenUiState },
        listUiStateProvider = { listUiState },
        addUiStateProvider = { addUiState },
        detailProvider = { tagDetail },
        detailUiStateProvider = { detailUiState },
        detailActionsUiStateProvider = { detailActionsUiState },
        tagMemoUiStateProvider = { tagMemoUiState },
        tagMemoListUiStateProvider = { tagMemoListUiState },
    )

    LaunchedFetch(
        navigator = navigator,
        detailViewModel = detailViewModel,
        memoViewModel = memoViewModel,
    )

    LaunchedScaffoldValue(
        navigator = navigator,
        onScaffoldValueChange = onScaffoldValueChange,
    )
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun LaunchedFetch(
    navigator: ThreePaneScaffoldNavigator<String?>,
    detailViewModel: TagDetailViewModel,
    memoViewModel: TagMemoViewModel,
) {
    LaunchedEffect(navigator.currentDestination?.content, detailViewModel) {
        detailViewModel.fetch(navigator.currentDestination?.content)
        memoViewModel.fetch(navigator.currentDestination?.content)
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun LaunchedScaffoldValue(
    navigator: ThreePaneScaffoldNavigator<String?>,
    onScaffoldValueChange: (ThreePaneScaffoldValue) -> Unit,
) {
    LaunchedEffect(navigator.scaffoldValue) {
        onScaffoldValueChange(navigator.scaffoldValue)
    }
}
