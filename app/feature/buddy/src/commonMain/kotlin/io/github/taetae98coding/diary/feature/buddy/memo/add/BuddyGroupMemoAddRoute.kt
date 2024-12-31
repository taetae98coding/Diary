package io.github.taetae98coding.diary.feature.buddy.memo.add

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.memo.add.MemoAddMultiPaneScaffold
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardUiState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun BuddyGroupMemoAddRoute(
	navigateUp: () -> Unit,
	modifier: Modifier = Modifier,
	addViewModel: BuddyGroupMemoAddViewModel = koinViewModel(),
) {
	val detailUiState by addViewModel.uiState.collectAsStateWithLifecycle()

	MemoAddMultiPaneScaffold(
		navigateUp = navigateUp,
		navigateToTagAdd = {},
		navigateToTagDetail = {},
		initialAddDateRange = addViewModel.route.dateRange,
		detailUiStateProvider = { detailUiState },
		primaryTagUiStateProvider = { TagCardUiState.Loading },
		tagUiStateProvider = { TagCardUiState.Loading },
		modifier = modifier,
	)
}
