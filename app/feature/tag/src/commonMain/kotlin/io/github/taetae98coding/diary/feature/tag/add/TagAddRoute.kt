package io.github.taetae98coding.diary.feature.tag.add

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailActionButton
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailFloatingButton
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailNavigationButton
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailScreen
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
		directive = navigator.scaffoldDirective,
		value = navigator.scaffoldValue,
		listPane = {
			AnimatedPane {
				val state = rememberTagDetailScreenAddState()
				val uiState by addViewModel.uiState.collectAsStateWithLifecycle()

				TagDetailScreen(
					state = state,
					titleProvider = { "태그 추가" },
					navigateButtonProvider = {
						TagDetailNavigationButton.NavigateUp(onNavigateUp = navigateUp)
					},
					actionButtonProvider = { TagDetailActionButton.None },
					floatingButtonProvider = { TagDetailFloatingButton.Add(onAdd = { addViewModel.add(state.tagDetail) }) },
					uiStateProvider = { uiState },
				)
			}
		},
		detailPane = {
		},
		modifier = modifier,
	)
}
