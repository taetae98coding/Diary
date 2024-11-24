package io.github.taetae98coding.diary.feature.memo.add

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.compose.back.KBackHandler
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailActionButton
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailFloatingButton
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailNavigationButton
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailScreen
import io.github.taetae98coding.diary.feature.memo.tag.MemoTagNavigationButton
import io.github.taetae98coding.diary.feature.memo.tag.MemoTagScreen
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun MemoAddRoute(
    navigateUp: () -> Unit,
    navigateToTagAdd: () -> Unit,
    navigateToTagDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    addViewModel: MemoAddViewModel = koinViewModel(),
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val navigator = rememberListDetailPaneScaffoldNavigator(scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo))

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective.copy(defaultPanePreferredWidth = 450.dp),
        value = navigator.scaffoldValue,
        listPane = {
            val state = rememberMemoDetailScreenAddState(
                initialStart = addViewModel.route.start,
                initialEndInclusive = addViewModel.route.endInclusive,
            )

            AnimatedPane {
                val uiState by addViewModel.uiState.collectAsStateWithLifecycle()
                val tagList by addViewModel.memoTagList.collectAsStateWithLifecycle()

                MemoDetailScreen(
                    state = state,
                    titleProvider = { "메모 추가" },
                    navigateButtonProvider = { MemoDetailNavigationButton.NavigateUp(onNavigateUp = navigateUp) },
                    actionButtonProvider = { MemoDetailActionButton.None },
                    floatingButtonProvider = { MemoDetailFloatingButton.Add { addViewModel.add(state.memoDetail) } },
                    uiStateProvider = { uiState },
                    onTagTitle = { navigator.navigateTo(ThreePaneScaffoldRole.Primary) },
                    onTag = navigateToTagDetail,
                    tagListProvider = { tagList },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val isNavigateUpVisible = remember(windowAdaptiveInfo) {
                    if (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                        !navigator.isListVisible()
                    } else {
                        true
                    }
                }

                val memoTagList by addViewModel.memoTagList.collectAsStateWithLifecycle()
                val tagList by addViewModel.tagList.collectAsStateWithLifecycle()

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
