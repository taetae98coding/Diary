package io.github.taetae98coding.diary.feature.memo.add

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.memo.add.MemoAddMultiPaneScaffold
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
	val detailUiState by addViewModel.uiState.collectAsStateWithLifecycle()
	val primaryTagUiState by addViewModel.primaryTagUiState.collectAsStateWithLifecycle()
	val tagUiState by addViewModel.tagUiState.collectAsStateWithLifecycle()

	MemoAddMultiPaneScaffold(
		navigateUp = navigateUp,
		navigateToTagAdd = navigateToTagAdd,
		navigateToTagDetail = navigateToTagDetail,
		initialAddDateRange = addViewModel.route.dateRange,
		detailUiStateProvider = { detailUiState },
		primaryTagUiStateProvider = { primaryTagUiState },
		tagUiStateProvider = { tagUiState },
		modifier = modifier,
	)
}
