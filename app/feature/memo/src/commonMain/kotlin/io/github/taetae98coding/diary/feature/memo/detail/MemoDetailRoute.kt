package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.memo.MemoDetailMultiPaneScaffold
import io.github.taetae98coding.diary.core.compose.memo.detail.rememberMemoDetailScaffoldDetailState
import io.github.taetae98coding.diary.core.design.system.icon.DeleteIcon
import io.github.taetae98coding.diary.core.design.system.icon.FinishIcon
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
	val memo by detailViewModel.memo.collectAsStateWithLifecycle()
	val detail by detailViewModel.detail.collectAsStateWithLifecycle()
	val state = rememberMemoDetailScaffoldDetailState(
		onDelete = navigateUp,
		onUpdate = navigateUp,
		detailProvider = { detail },
	)
	val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()
	val primaryTagList by detailTagViewModel.primaryTagList.collectAsStateWithLifecycle()
	val tagList by detailTagViewModel.tagList.collectAsStateWithLifecycle()

	MemoDetailMultiPaneScaffold(
		onNavigateUp = { detailViewModel.update(state.detail) },
		onDetailTag = navigateToTagDetail,
		detailScaffoldStateProvider = { state },
		detailUiStateProvider = { uiState },
		detailTagListProvider = { primaryTagList },
		detailTitle = { Text(text = detail?.title.orEmpty()) },
		onTagAdd = navigateToTagAdd,
		tagListProvider = { tagList },
		modifier = modifier,
		detailActions = {
			val isFinished by remember { derivedStateOf { memo?.isFinish == true } }

			IconToggleButton(
				checked = isFinished,
				onCheckedChange = { detailViewModel.onFinishChange(it) },
			) {
				FinishIcon()
			}

			IconButton(onClick = detailViewModel::delete) {
				DeleteIcon()
			}
		},
	)
}
