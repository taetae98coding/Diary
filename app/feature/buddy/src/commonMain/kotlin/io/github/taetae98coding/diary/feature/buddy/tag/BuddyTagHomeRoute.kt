package io.github.taetae98coding.diary.feature.buddy.tag

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.tag.TagListMultiPaneScaffold
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldActionsUiState
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldUiState
import io.github.taetae98coding.diary.core.compose.tag.list.TagListScaffoldUiState
import io.github.taetae98coding.diary.core.compose.tag.list.TagListUiState
import io.github.taetae98coding.diary.core.compose.tag.memo.TagMemoListUiState
import io.github.taetae98coding.diary.core.compose.tag.memo.TagMemoScaffoldUiState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun BuddyTagHomeRoute(
	navigateUp: () -> Unit,
	navigateToMemoAdd: (String) -> Unit,
	navigateToMemoDetail: (String) -> Unit,
	modifier: Modifier = Modifier,
	addViewModel: BuddyTagAddViewModel = koinViewModel(),
) {
	val navigator = rememberListDetailPaneScaffoldNavigator<String?>()
	val addUiState by addViewModel.uiState.collectAsStateWithLifecycle()

	TagListMultiPaneScaffold(
		navigateToMemoAdd = navigateToMemoAdd,
		navigateToMemoDetail = navigateToMemoDetail,
		navigator = navigator,
		listScaffoldUiStateProvider = { TagListScaffoldUiState() },
		listUiStateProvider = { TagListUiState.Loading },
		addUiStateProvider = { addUiState },
		detailProvider = { null },
		detailUiStateProvider = { TagDetailScaffoldUiState.Detail() },
		detailActionsUiStateProvider = { TagDetailScaffoldActionsUiState() },
		tagMemoListUiStateProvider = { TagMemoListUiState.Loading },
		tagMemoUiStateProvider = { TagMemoScaffoldUiState() },
		modifier = modifier,
	)
}
