package io.github.taetae98coding.diary.feature.buddy.memo.detail

import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
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

@Composable
internal fun BuddyGroupMemoDetailRoute(
	navigateUp: () -> Unit,
	modifier: Modifier = Modifier,
	detailViewModel: BuddyGroupMemoDetailViewModel = koinViewModel(),
) {
	val memo by detailViewModel.memo.collectAsStateWithLifecycle()
	val detail by detailViewModel.detail.collectAsStateWithLifecycle()
	val state = rememberMemoDetailScaffoldDetailState(
		onDelete = navigateUp,
		onUpdate = navigateUp,
		detailProvider = { detail },
	)
	val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()

	MemoDetailMultiPaneScaffold(
		onNavigateUp = { detailViewModel.update(state.detail) },
		onDetailTag = { },
		detailScaffoldStateProvider = { state },
		detailUiStateProvider = { uiState },
		detailTagListProvider = { null },
		detailTitle = { Text(text = detail?.title.orEmpty()) },
		onTagAdd = {},
		tagListProvider = { null },
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
