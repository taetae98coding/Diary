package io.github.taetae98coding.diary.core.compose.memo.add

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffold
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldFloatingButton
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldNavigationIcon
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldUiState
import io.github.taetae98coding.diary.core.compose.memo.tag.MemoTagNavigationIcon
import io.github.taetae98coding.diary.core.compose.memo.tag.MemoTagScaffold
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardUiState
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
public fun MemoAddMultiPaneScaffold(
	navigateUp: () -> Unit,
	navigateToTagAdd: () -> Unit,
	navigateToTagDetail: (String) -> Unit,
	initialAddDateRange: ClosedRange<LocalDate>?,
	detailUiStateProvider: () -> MemoDetailScaffoldUiState.Add,
	primaryTagUiStateProvider: () -> TagCardUiState,
	tagUiStateProvider: () -> TagCardUiState,
	modifier: Modifier = Modifier,
) {
	val navigator = rememberListDetailPaneScaffoldNavigator()

	ListDetailPaneScaffold(
		directive = navigator.scaffoldDirective.copy(defaultPanePreferredWidth = 500.dp),
		value = navigator.scaffoldValue,
		listPane = {
			val state = rememberMemoDetailScaffoldAddState(initialDateRange = initialAddDateRange)

			AnimatedPane {
				MemoDetailScaffold(
					state = state,
					uiStateProvider = detailUiStateProvider,
					primaryTagUiStateProvider = primaryTagUiStateProvider,
					onTagTitle = { navigator.navigateTo(ThreePaneScaffoldRole.Primary) },
					onTag = navigateToTagDetail,
					titleProvider = { "메모 추가" },
					navigationIconProvider = { MemoDetailScaffoldNavigationIcon.NavigateUp(navigateUp) },
					floatingButtonProvider = {
						val uiState = detailUiStateProvider()

						MemoDetailScaffoldFloatingButton.Add(
							isInProgress = uiState.isAddInProgress,
							add = { uiState.add(state.detail) },
						)
					},
				)
			}
		},
		detailPane = {
			AnimatedPane {
				MemoTagScaffold(
					onTagAdd = navigateToTagAdd,
					primaryTagUiStateProvider = primaryTagUiStateProvider,
					tagUiStateProvider = tagUiStateProvider,
					navigationIcon = {
						if (navigator.isListVisible()) {
							MemoTagNavigationIcon.None
						} else {
							MemoTagNavigationIcon.NavigateUp(navigator::navigateBack)
						}
					},
				)
			}
		},
		modifier = modifier,
	)
}
