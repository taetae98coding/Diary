package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.compose.back.KBackHandler
import io.github.taetae98coding.diary.feature.memo.tag.MemoTagNavigationButton
import io.github.taetae98coding.diary.feature.memo.tag.MemoTagScreen
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun MemoDetailRoute(
    navigateUp: () -> Unit,
    navigateToTagAdd: () -> Unit,
    navigateToTagDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    detailViewModel: MemoDetailViewModel = koinViewModel(),
    detailTagViewModel: MemoDetailTagViewModel = koinViewModel(),
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val navigator = rememberListDetailPaneScaffoldNavigator(scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo))

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            val detail by detailViewModel.detail.collectAsStateWithLifecycle()
            val state = rememberMemoDetailScreenDetailState(
                onDelete = navigateUp,
                onUpdate = navigateUp,
                detailProvider = { detail },
            )

            AnimatedPane {
                val actionButton by detailViewModel.actionButton.collectAsStateWithLifecycle()
                val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()
                val tagList by detailTagViewModel.memoTagUiStateList.collectAsStateWithLifecycle()

                MemoDetailScreen(
                    state = state,
                    titleProvider = { detail?.title },
                    navigateButtonProvider = { MemoDetailNavigationButton.NavigateUp(onNavigateUp = { detailViewModel.update(state.memoDetail) }) },
                    actionButtonProvider = { actionButton },
                    floatingButtonProvider = { MemoDetailFloatingButton.None },
                    uiStateProvider = { uiState },
                    onTagTitle = { navigator.navigateTo(ThreePaneScaffoldRole.Primary) },
                    onTag = navigateToTagDetail,
                    tagListProvider = { tagList },
                )
            }
        },
        detailPane = {
            val isNavigateUpVisible = remember(windowAdaptiveInfo) {
                if (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                    !navigator.isListVisible()
                } else {
                    true
                }
            }

            AnimatedPane {
                val memoTagList by detailTagViewModel.memoTagUiStateList.collectAsStateWithLifecycle()
                val tagList by detailTagViewModel.tagList.collectAsStateWithLifecycle()

                MemoTagScreen(
                    navigateButtonProvider = {
                        if (isNavigateUpVisible) {
                            MemoTagNavigationButton.NavigateUp(onNavigateUp = navigator::navigateBack)
                        } else {
                            MemoTagNavigationButton.None
                        }
                    },
                    onTagAdd = navigateToTagAdd,
                    memoTagListProvider = { memoTagList },
                    tagListProvider = { tagList },
                )
            }
        },
        modifier = modifier,
    )

    KBackHandler(
        isEnabled = navigator.canNavigateBack(),
        onBack = navigator::navigateBack,
    )
}
