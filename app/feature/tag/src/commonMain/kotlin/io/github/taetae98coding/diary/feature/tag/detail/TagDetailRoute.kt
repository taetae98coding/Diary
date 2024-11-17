package io.github.taetae98coding.diary.feature.tag.detail

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
internal fun TagDetailRoute(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    detailViewModel: TagDetailViewModel = koinViewModel(),
) {
    val navigator = rememberListDetailPaneScaffoldNavigator()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                val tagDetail by detailViewModel.tagDetail.collectAsStateWithLifecycle()
                val state = rememberTagDetailScreenDetailState(
                    onUpdate = navigateUp,
                    onDelete = navigateUp,
                    detailProvider = { tagDetail },
                )
                val actionButton by detailViewModel.actionButton.collectAsStateWithLifecycle()
                val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()

                TagDetailScreen(
                    state = state,
                    titleProvider = { tagDetail?.title },
                    navigateButtonProvider = {
                        TagDetailNavigationButton.NavigateUp(
                            onNavigateUp = { detailViewModel.update(state.tagDetail) },
                        )
                    },
                    actionButtonProvider = { actionButton },
                    floatingButtonProvider = { TagDetailFloatingButton.None },
                    uiStateProvider = { uiState },
                )
            }
        },
        detailPane = {

        },
        modifier = modifier,
    )
}
