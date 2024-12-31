package io.github.taetae98coding.diary.feature.tag.add

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.tag.add.rememberTagDetailScaffoldAddState
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffold
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldActions
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldFloatingButton
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldNavigationIcon
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun TagAddRoute(
	navigateUp: () -> Unit,
	modifier: Modifier = Modifier,
	addViewModel: TagAddViewModel = koinViewModel(),
) {
	val navigator = rememberListDetailPaneScaffoldNavigator()

	ListDetailPaneScaffold(
		directive = navigator.scaffoldDirective.copy(defaultPanePreferredWidth = 500.dp),
		value = navigator.scaffoldValue,
		listPane = {
			AnimatedPane {
				val state = rememberTagDetailScaffoldAddState()
				val uiState by addViewModel.uiState.collectAsStateWithLifecycle()

				TagDetailScaffold(
					state = state,
					titleProvider = { "태그 추가" },
					navigationIconProvider = { TagDetailScaffoldNavigationIcon.NavigateUp(navigateUp = navigateUp) },
					actionsProvider = { TagDetailScaffoldActions.None },
					floatingButtonProvider = {
						TagDetailScaffoldFloatingButton.Add(
							isInProgress = uiState.isAddInProgress,
							add = { uiState.add(state.tagDetail) },
						)
					},
					uiStateProvider = { uiState },
				)
			}
		},
		detailPane = {
		},
		modifier = modifier,
	)
}
