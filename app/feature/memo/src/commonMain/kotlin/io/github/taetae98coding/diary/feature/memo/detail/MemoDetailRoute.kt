package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.memo.MemoDetailMultiPaneScaffold
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun MemoDetailRoute(
	navigateUp: () -> Unit,
	navigateToTagAdd: () -> Unit,
	navigateToTagDetail: (String) -> Unit,
	modifier: Modifier = Modifier,
	detailViewModel: MemoDetailViewModel = koinViewModel(),
	detailTagViewModel: MemoDetailTagViewModel = koinViewModel(),
) {
	val detail by detailViewModel.detail.collectAsStateWithLifecycle()
	val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()
	val actionsUiState by detailViewModel.actionsUiState.collectAsStateWithLifecycle()
	val primaryTagUiState by detailTagViewModel.primaryTagUiState.collectAsStateWithLifecycle()
	val tagUiState by detailTagViewModel.tagUiState.collectAsStateWithLifecycle()

	MemoDetailMultiPaneScaffold(
		navigateUp = navigateUp,
		navigateToTagAdd = navigateToTagAdd,
		navigateToTagDetail = navigateToTagDetail,
		detailProvider = { detail },
		uiStateProvider = { uiState },
		detailActionsUiStateProvider = { actionsUiState },
		primaryTagUiStateProvider = { primaryTagUiState },
		tagUiStateProvider = { tagUiState },
	)
}
