package io.github.taetae98coding.diary.core.compose.memo

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.IconButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.compose.back.KBackHandler
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffold
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldState
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldUiState
import io.github.taetae98coding.diary.core.compose.memo.tag.MemoTagScaffold
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardItemUiState
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
public fun MemoDetailMultiPaneScaffold(
	onNavigateUp: () -> Unit,
	onDetailTag: (String) -> Unit,
	detailScaffoldStateProvider: () -> MemoDetailScaffoldState,
	detailUiStateProvider: () -> MemoDetailScaffoldUiState,
	detailTagListProvider: () -> List<TagCardItemUiState>?,
	detailTitle: @Composable () -> Unit,
	onTagAdd: () -> Unit,
	tagListProvider: () -> List<TagCardItemUiState>?,
	modifier: Modifier = Modifier,
	detailActions: @Composable RowScope.() -> Unit = {},
	detailFloatingActionButton: @Composable () -> Unit = {},
) {
	val windowAdaptiveInfo = currentWindowAdaptiveInfo()
	val navigator = rememberListDetailPaneScaffoldNavigator(scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo))

	ListDetailPaneScaffold(
		directive = navigator.scaffoldDirective.copy(defaultPanePreferredWidth = 500.dp),
		value = navigator.scaffoldValue,
		listPane = {
			AnimatedPane {
				MemoDetailScaffold(
					onTagTitle = { navigator.navigateTo(ThreePaneScaffoldRole.Primary) },
					onTag = onDetailTag,
					state = detailScaffoldStateProvider(),
					uiStateProvider = detailUiStateProvider,
					tagListProvider = detailTagListProvider,
					title = detailTitle,
					navigationIcon = {
						IconButton(onClick = onNavigateUp) {
							NavigateUpIcon()
						}
					},
					actions = detailActions,
					floatingActionButton = detailFloatingActionButton,
				)
			}
		},
		detailPane = {
			AnimatedPane {
				MemoTagScaffold(
					onTagAdd = onTagAdd,
					navigationIcon = {
						if (!navigator.isListVisible()) {
							IconButton(onClick = navigator::navigateBack) {
								NavigateUpIcon()
							}
						}
					},
					primaryTagListProvider = detailTagListProvider,
					tagListProvider = tagListProvider,
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
