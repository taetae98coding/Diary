package io.github.taetae98coding.diary.core.compose.memo

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.compose.back.KBackHandler
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffold
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldActions
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldActionsUiState
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldNavigationIcon
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldUiState
import io.github.taetae98coding.diary.core.compose.memo.detail.rememberMemoDetailScaffoldDetailState
import io.github.taetae98coding.diary.core.compose.memo.tag.MemoTagNavigationIcon
import io.github.taetae98coding.diary.core.compose.memo.tag.MemoTagScaffold
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardUiState
import io.github.taetae98coding.diary.core.model.memo.MemoDetail

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
public fun MemoDetailMultiPaneScaffold(
	navigateUp: () -> Unit,
	navigateToTagAdd: () -> Unit,
	navigateToTagDetail: (String) -> Unit,
	detailProvider: () -> MemoDetail?,
	uiStateProvider: () -> MemoDetailScaffoldUiState.Detail,
	detailActionsUiStateProvider: () -> MemoDetailScaffoldActionsUiState,
	primaryTagUiStateProvider: () -> TagCardUiState,
	tagUiStateProvider: () -> TagCardUiState,
	modifier: Modifier = Modifier,
) {
	val navigator = rememberListDetailPaneScaffoldNavigator()

	ListDetailPaneScaffold(
		directive = navigator.scaffoldDirective.copy(defaultPanePreferredWidth = 500.dp),
		value = navigator.scaffoldValue,
		listPane = {
			AnimatedPane {
				val state = rememberMemoDetailScaffoldDetailState(
					onDelete = navigateUp,
					onUpdate = navigateUp,
					detailProvider = detailProvider,
				)

				MemoDetailScaffold(
					state = state,
					uiStateProvider = uiStateProvider,
					primaryTagUiStateProvider = primaryTagUiStateProvider,
					onTagTitle = { navigator.navigateTo(ThreePaneScaffoldRole.Primary) },
					onTag = navigateToTagDetail,
					titleProvider = { detailProvider()?.title },
					navigationIconProvider = {
						MemoDetailScaffoldNavigationIcon.NavigateUp(
							navigateUp = {
								uiStateProvider().update(state.detail)
							},
						)
					},
					actionsProvider = {
						val uiState = detailActionsUiStateProvider()

						MemoDetailScaffoldActions.FinishAndDelete(
							isFinish = uiState.isFinish,
							finish = uiState.finish,
							restart = uiState.restart,
							delete = uiState.delete,
						)
					},
				)
			}
		},
		detailPane = {
			AnimatedPane {
				MemoTagScaffold(
					onTagAdd = navigateToTagAdd,
					navigationIcon = {
						if (!navigator.isListVisible()) {
							MemoTagNavigationIcon.NavigateUp(navigateUp)
						} else {
							MemoTagNavigationIcon.None
						}
					},
					primaryTagUiStateProvider = primaryTagUiStateProvider,
					tagUiStateProvider = tagUiStateProvider,
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
