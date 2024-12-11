package io.github.taetae98coding.diary.feature.buddy.memo.add

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.button.FloatingAddButton
import io.github.taetae98coding.diary.core.compose.memo.MemoDetailMultiPaneScaffold
import io.github.taetae98coding.diary.core.compose.memo.add.rememberMemoDetailScaffoldAddState
import io.github.taetae98coding.diary.core.compose.topbar.TopBarTitle
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun BuddyGroupMemoAddRoute(
	navigateUp: () -> Unit,
	modifier: Modifier = Modifier,
	addViewModel: BuddyGroupMemoAddViewModel = koinViewModel(),
) {
	val detailScaffoldState = rememberMemoDetailScaffoldAddState(
		initialStart = addViewModel.route.start,
		initialEndInclusive = addViewModel.route.endInclusive,
	)
	val detailUiState by addViewModel.uiState.collectAsStateWithLifecycle()

	MemoDetailMultiPaneScaffold(
		onNavigateUp = navigateUp,
		onDetailTag = { },
		detailScaffoldStateProvider = { detailScaffoldState },
		detailUiStateProvider = { detailUiState },
		detailTagListProvider = { null },
		detailTitle = { TopBarTitle(text = "그룹 메모 추가") },
		onTagAdd = {},
		tagListProvider = { null },
		modifier = modifier,
		detailFloatingActionButton = {
			val isProgress by remember { derivedStateOf { detailUiState.isProgress } }

			FloatingAddButton(
				onClick = { addViewModel.add(detailScaffoldState.detail) },
				progressProvider = { isProgress },
			)
		},
	)
}
