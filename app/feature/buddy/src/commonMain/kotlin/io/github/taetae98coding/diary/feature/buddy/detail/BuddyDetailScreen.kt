package io.github.taetae98coding.diary.feature.buddy.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.compose.button.FloatingAddButton
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponent
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.buddy.Buddy
import io.github.taetae98coding.diary.feature.buddy.common.BuddyBottomSheetUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BuddyDetailScreen(
	onNavigateUp: () -> Unit,
	onAdd: () -> Unit,
	state: BuddyDetailScreenState,
	uiStateProvider: () -> BuddyDetailScreenUiState,
	buddyUiStateProvider: () -> List<Buddy>?,
	buddyBottomSheetUiStateProvider: () -> BuddyBottomSheetUiState,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = { Text("그룹 추가") },
				navigationIcon = {
					IconButton(onClick = onNavigateUp) {
						NavigateUpIcon()
					}
				},
			)
		},
		snackbarHost = { SnackbarHost(hostState = state.hostState) },
		floatingActionButton = {
			val isProgress by remember { derivedStateOf { uiStateProvider().isProgress } }

			FloatingAddButton(
				onClick = onAdd,
				progressProvider = { isProgress },
			)
		},
	) {
		Content(
			state = state,
			buddyUiStateProvider = buddyUiStateProvider,
			buddyBottomSheetUiStateProvider = buddyBottomSheetUiStateProvider,
			modifier = Modifier
				.fillMaxSize()
				.padding(it)
				.padding(DiaryTheme.dimen.screenPaddingValues),
		)
	}

	Message(
		state = state,
		uiStateProvider = uiStateProvider,
	)
}

@Composable
private fun Message(
	state: BuddyDetailScreenState,
	uiStateProvider: () -> BuddyDetailScreenUiState,
) {
	val uiState = uiStateProvider()

	LaunchedEffect(
		uiState.isAdd,
		uiState.isExit,
		uiState.isUpdate,
		uiState.isTitleBlankError,
		uiState.isNetworkError,
		uiState.isUnknownError,
	) {
		if (!uiState.hasMessage) return@LaunchedEffect

		when {
			uiState.isAdd -> {
				state.showMessage("그룹 추가 ${Emoji.congratulate.random()}")
				state.clearInput()
				state.requestTitleFocus()
			}

			uiState.isExit -> {
//                if (state is TagDetailScreenState.Detail) {
//                    state.onDelete()
//                }
			}

			uiState.isUpdate -> {
//                if (state is TagDetailScreenState.Detail) {
//                    state.onUpdate()
//                }
			}

			uiState.isTitleBlankError -> {
				state.showMessage("제목을 입력해 주세요 ${Emoji.check.random()}")
				state.requestTitleFocus()
				state.titleError()
			}

			uiState.isNetworkError -> {
				state.showMessage("네트워크 상태를 확인해 주세요 ${Emoji.error.random()}")
			}

			uiState.isUnknownError -> state.showMessage("알 수 없는 에러가 발생했어요 잠시 후 다시 시도해 주세요 ${Emoji.error.random()}")
		}

		uiState.onMessageShow()
	}
}

@Composable
private fun Content(
	state: BuddyDetailScreenState,
	buddyUiStateProvider: () -> List<Buddy>?,
	buddyBottomSheetUiStateProvider: () -> BuddyBottomSheetUiState,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = Modifier
			.verticalScroll(rememberScrollState())
			.then(modifier),
		verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
	) {
		DiaryComponent(
			state = state.componentState,
		)

		BuddyGroup(
			state = state.buddyBottomSheetState,
			buddyUiStateProvider = buddyUiStateProvider,
			bottomSheetUiState = buddyBottomSheetUiStateProvider,
			modifier = Modifier.fillMaxWidth(),
		)
	}
}
