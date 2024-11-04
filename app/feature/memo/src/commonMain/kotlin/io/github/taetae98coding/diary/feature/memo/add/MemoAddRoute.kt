package io.github.taetae98coding.diary.feature.memo.add

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.resources.Res
import io.github.taetae98coding.diary.core.resources.memo_add
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailActionButton
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailFloatingButton
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailNavigationButton
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailScreen
import io.github.taetae98coding.diary.feature.memo.detail.rememberMemoDetailScreenAddState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun MemoAddRoute(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    addViewModel: MemoAddViewModel = koinViewModel(),
) {
    val navigator = rememberListDetailPaneScaffoldNavigator()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                val state = rememberMemoDetailScreenAddState(
                    initialStart = addViewModel.route.start,
                    initialEndInclusive = addViewModel.route.endInclusive,
                )
                val title = stringResource(Res.string.memo_add)
                val uiState by addViewModel.uiState.collectAsStateWithLifecycle()

                MemoDetailScreen(
                    state = state,
                    titleProvider = { title },
                    navigateButtonProvider = { MemoDetailNavigationButton.NavigateUp(onNavigateUp = navigateUp) },
                    actionButtonProvider = { MemoDetailActionButton.None },
                    floatingButtonProvider = { MemoDetailFloatingButton.Add { addViewModel.add(state.memoDetail) } },
                    uiStateProvider = { uiState },
                )
            }
        },
        detailPane = {
        },
        modifier = modifier,
    )
}
