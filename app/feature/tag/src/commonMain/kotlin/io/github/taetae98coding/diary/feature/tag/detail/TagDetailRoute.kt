package io.github.taetae98coding.diary.feature.tag.detail

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.compose.back.KBackHandler
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffold
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldActions
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldNavigationIcon
import io.github.taetae98coding.diary.core.compose.tag.detail.rememberTagDetailScaffoldDetailState
import io.github.taetae98coding.diary.core.compose.tag.memo.TagMemoNavigateIcon
import io.github.taetae98coding.diary.core.compose.tag.memo.TagMemoScaffold
import io.github.taetae98coding.diary.core.compose.tag.memo.rememberTagMemoScaffoldState
import io.github.taetae98coding.diary.feature.tag.memo.TagMemoViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun TagDetailRoute(
    navigateUp: () -> Unit,
    navigateToMemoAdd: () -> Unit,
    navigateToMemoDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    detailViewModel: TagDetailViewModel = koinViewModel(),
    memoViewModel: TagMemoViewModel = koinViewModel(),
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val navigator = rememberListDetailPaneScaffoldNavigator(scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo))

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective.copy(defaultPanePreferredWidth = 500.dp),
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                val tagDetail by detailViewModel.tagDetail.collectAsStateWithLifecycle()
                val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()
                val actionsUiState by detailViewModel.actionsUiState.collectAsStateWithLifecycle()
                val state = rememberTagDetailScaffoldDetailState(
                    onUpdate = navigateUp,
                    onDelete = navigateUp,
                    navigateToMemo = { navigator.navigateTo(ThreePaneScaffoldRole.Primary) },
                    detailProvider = { tagDetail },
                )

                TagDetailScaffold(
                    state = state,
                    uiStateProvider = { uiState },
                    titleProvider = { tagDetail?.title },
                    navigationIconProvider = {
                        TagDetailScaffoldNavigationIcon.NavigateUp(
                            navigateUp = { detailViewModel.update(state.tagDetail) },
                        )
                    },
                    actionsProvider = {
                        TagDetailScaffoldActions.FinishAndDelete(
                            isFinish = actionsUiState.isFinish,
                            finish = actionsUiState.finish,
                            restart = actionsUiState.restart,
                            delete = actionsUiState.delete,
                        )
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val uiState by memoViewModel.uiState.collectAsStateWithLifecycle()
                val memoList by memoViewModel.memoList.collectAsStateWithLifecycle()

                TagMemoScaffold(
                    state = rememberTagMemoScaffoldState(),
                    uiStateProvider = { uiState },
                    listUiStateProvider = { memoList },
                    onAdd = navigateToMemoAdd,
                    onMemo = navigateToMemoDetail,
                    navigateIconProvider = {
                        if (navigator.isListVisible()) {
                            TagMemoNavigateIcon.None
                        } else {
                            TagMemoNavigateIcon.NavigateUp(navigator::navigateBack)
                        }
                    },
                )
            }
        },
        modifier = modifier,
    )

    NavigateUp(navigator = navigator)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun NavigateUp(
    navigator: ThreePaneScaffoldNavigator<*>,
) {
    KBackHandler(
        isEnabled = navigator.canNavigateBack(),
        onBack = navigator::navigateBack,
    )
}
