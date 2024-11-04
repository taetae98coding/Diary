package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun MemoDetailRoute(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    detailViewModel: MemoDetailViewModel = koinViewModel(),
) {
    val navigator = rememberListDetailPaneScaffoldNavigator()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                val detail by detailViewModel.detail.collectAsStateWithLifecycle()
                val state = rememberMemoDetailScreenDetailState(
                    onDelete = navigateUp,
                    onUpdate = navigateUp,
                    detailProvider = { detail },
                )
                val actionButton by detailViewModel.actionButton.collectAsStateWithLifecycle()
                val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()

                MemoDetailScreen(
                    state = state,
                    titleProvider = { detail?.title },
                    navigateButtonProvider = { MemoDetailNavigationButton.NavigateUp(onNavigateUp = { detailViewModel.update(state.memoDetail) }) },
                    actionButtonProvider = { actionButton },
                    floatingButtonProvider = { MemoDetailFloatingButton.None },
                    uiStateProvider = { uiState },
                )
            }
        },
        detailPane = {
        },
        modifier = modifier,
    )
}
