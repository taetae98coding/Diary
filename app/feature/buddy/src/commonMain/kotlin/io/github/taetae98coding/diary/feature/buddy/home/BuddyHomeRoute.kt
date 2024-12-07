package io.github.taetae98coding.diary.feature.buddy.home

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.feature.buddy.add.BuddyAddViewModel
import io.github.taetae98coding.diary.feature.buddy.add.rememberBuddyDetailScreenAddState
import io.github.taetae98coding.diary.feature.buddy.detail.BuddyDetailScreen
import io.github.taetae98coding.diary.feature.buddy.detail.BuddyDetailScreenState
import io.github.taetae98coding.diary.feature.buddy.list.BuddyListScreen
import io.github.taetae98coding.diary.feature.buddy.list.BuddyListViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun BuddyHomeRoute(
	onScaffoldValueChange: (ThreePaneScaffoldValue) -> Unit,
	modifier: Modifier = Modifier,
	listViewModel: BuddyListViewModel = koinViewModel(),
	addViewModel: BuddyAddViewModel = koinViewModel(),
) {
	val navigator = rememberListDetailPaneScaffoldNavigator<String?>()

	ListDetailPaneScaffold(
		directive = navigator.scaffoldDirective.copy(defaultPanePreferredWidth = 500.dp),
		value = navigator.scaffoldValue,
		listPane = {
			AnimatedPane {
				val groupList by listViewModel.groupList.collectAsStateWithLifecycle()

				BuddyListScreen(
					groupListProvider = { groupList },
					onAdd = { navigator.navigateTo(ThreePaneScaffoldRole.Primary) },
				)
			}
		},
		detailPane = {
			AnimatedPane {
				val state = rememberBuddyDetailScreenAddState()
				val uiState by addViewModel.uiState.collectAsStateWithLifecycle()
				val buddyUiState by addViewModel.buddyUiState.collectAsStateWithLifecycle()
				val buddyBottomSheetState by addViewModel.buddyBottomSheetUiState.collectAsStateWithLifecycle()

				BuddyDetailScreen(
					onNavigateUp = navigator::navigateBack,
					onAdd = { addViewModel.add(state.detail) },
					state = state,
					uiStateProvider = { uiState },
					buddyUiStateProvider = { buddyUiState },
					buddyBottomSheetUiStateProvider = { buddyBottomSheetState },
				)

				FetchAccount(
					addViewModel = addViewModel,
					state = state,
				)
			}
		},
		modifier = modifier,
	)

	LaunchedScaffoldValue(
		navigator = navigator,
		onScaffoldValueChange = onScaffoldValueChange,
	)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun LaunchedScaffoldValue(
	navigator: ThreePaneScaffoldNavigator<String?>,
	onScaffoldValueChange: (ThreePaneScaffoldValue) -> Unit,
) {
	LaunchedEffect(navigator.scaffoldValue) {
		onScaffoldValueChange(navigator.scaffoldValue)
	}
}

@Composable
private fun FetchAccount(
	addViewModel: BuddyAddViewModel,
	state: BuddyDetailScreenState,
) {
	LaunchedEffect(state.buddyBottomSheetState.email) {
		addViewModel.fetch(state.buddyBottomSheetState.email)
	}
}
