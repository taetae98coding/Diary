package io.github.taetae98coding.diary.feature.tag.detail

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.feature.tag.memo.TagMemoNavigateButton
import io.github.taetae98coding.diary.feature.tag.memo.TagMemoScreen
import io.github.taetae98coding.diary.feature.tag.memo.TagMemoViewModel
import io.github.taetae98coding.diary.feature.tag.memo.rememberTagMemoScreenState
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
				val state = rememberTagDetailScreenDetailState(
					onUpdate = navigateUp,
					onDelete = navigateUp,
					onMemo = { navigator.navigateTo(ThreePaneScaffoldRole.Primary) },
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
			val isNavigateUpVisible by remember(windowAdaptiveInfo) {
				derivedStateOf {
					if (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
						!navigator.isListVisible()
					} else {
						true
					}
				}
			}

			val uiState by memoViewModel.uiState.collectAsStateWithLifecycle()
			val list by memoViewModel.memoList.collectAsStateWithLifecycle()

			TagMemoScreen(
				state = rememberTagMemoScreenState(),
				navigateButtonProvider = {
					if (isNavigateUpVisible) {
						TagMemoNavigateButton.NavigateUp(onNavigateUp = navigator::navigateBack)
					} else {
						TagMemoNavigateButton.None
					}
				},
				uiStateProvider = { uiState },
				onAdd = navigateToMemoAdd,
				listProvider = { list },
				onMemo = navigateToMemoDetail,
			)
		},
		modifier = modifier,
	)
}
