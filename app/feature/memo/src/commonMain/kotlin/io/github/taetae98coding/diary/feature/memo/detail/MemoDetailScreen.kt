package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.compose.button.FloatingAddButton
import io.github.taetae98coding.diary.core.design.system.diary.color.DiaryColor
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponent
import io.github.taetae98coding.diary.core.design.system.diary.date.DiaryDate
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.icon.ChevronRightIcon
import io.github.taetae98coding.diary.core.design.system.icon.DeleteIcon
import io.github.taetae98coding.diary.core.design.system.icon.FinishIcon
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.memo.tag.PrimaryMemoTag
import io.github.taetae98coding.diary.feature.memo.tag.TagFlow
import io.github.taetae98coding.diary.feature.memo.tag.TagUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MemoDetailScreen(
	state: MemoDetailScreenState,
	titleProvider: () -> String?,
	navigateButtonProvider: () -> MemoDetailNavigationButton,
	actionButtonProvider: () -> MemoDetailActionButton,
	floatingButtonProvider: () -> MemoDetailFloatingButton,
	uiStateProvider: () -> MemoDetailScreenUiState,
	onTagTitle: () -> Unit,
	onTag: (String) -> Unit,
	tagListProvider: () -> List<TagUiState>?,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = {
					titleProvider()?.let {
						Text(
							text = it,
							modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
							maxLines = 1,
						)
					}
				},
				navigationIcon = {
					when (val button = navigateButtonProvider()) {
						is MemoDetailNavigationButton.NavigateUp -> {
							IconButton(onClick = button.onNavigateUp) {
								NavigateUpIcon()
							}
						}

						is MemoDetailNavigationButton.None -> Unit
					}
				},
				actions = {
					when (val button = actionButtonProvider()) {
						is MemoDetailActionButton.FinishAndDetail -> {
							IconToggleButton(
								checked = button.isFinish,
								onCheckedChange = button.onFinishChange,
							) {
								FinishIcon()
							}

							IconButton(onClick = button.delete) {
								DeleteIcon()
							}
						}

						else -> Unit
					}
				},
			)
		},
		snackbarHost = { SnackbarHost(hostState = state.hostState) },
		floatingActionButton = {
			when (val button = floatingButtonProvider()) {
				is MemoDetailFloatingButton.Add -> {
					val isProgress by remember { derivedStateOf { uiStateProvider().isProgress } }

					FloatingAddButton(
						onClick = button.onAdd,
						progressProvider = { isProgress },
					)
				}

				is MemoDetailFloatingButton.None -> Unit
			}
		},
	) {
		Content(
			state = state,
			onTagTitle = onTagTitle,
			onTag = onTag,
			tagListProvider = tagListProvider,
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

	LaunchedFocus(state = state)
}

@Composable
private fun Message(
	state: MemoDetailScreenState,
	uiStateProvider: () -> MemoDetailScreenUiState,
) {
	val uiState = uiStateProvider()

	LaunchedEffect(
		uiState.isAdd,
		uiState.isDelete,
		uiState.isUpdate,
		uiState.isTitleBlankError,
		uiState.isUnknownError,
	) {
		if (!uiState.hasMessage) return@LaunchedEffect

		when {
			uiState.isAdd -> {
				state.showMessage("메모 추가 ${Emoji.congratulate.random()}")
				state.clearInput()
				state.requestTitleFocus()
			}

			uiState.isDelete -> {
				if (state is MemoDetailScreenState.Detail) {
					state.onDelete()
				}
			}

			uiState.isUpdate -> {
				if (state is MemoDetailScreenState.Detail) {
					state.onUpdate()
				}
			}

			uiState.isTitleBlankError -> {
				state.showMessage("제목을 입력해 주세요 ${Emoji.check.random()}")
				state.titleError()
			}

			uiState.isUnknownError -> state.showMessage("알 수 없는 에러가 발생했어요 잠시 후 다시 시도해 주세요 ${Emoji.error.random()}")
		}

		uiState.onMessageShow()
	}
}

@Composable
private fun LaunchedFocus(
	state: MemoDetailScreenState,
) {
	LaunchedEffect(state) {
		if (state is MemoDetailScreenState.Add) {
			state.requestTitleFocus()
		}
	}
}

@Composable
private fun Content(
	state: MemoDetailScreenState,
	onTagTitle: () -> Unit,
	onTag: (String) -> Unit,
	tagListProvider: () -> List<TagUiState>?,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = Modifier
			.verticalScroll(state = rememberScrollState())
			.then(modifier),
		verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
	) {
		DiaryComponent(state = state.componentState)
		DiaryDate(state = state.dateState)
		InternalDiaryTag(
			onTitle = onTagTitle,
			onTag = onTag,
			listProvider = tagListProvider,
		)
		InternalDiaryColor(state = state)
	}
}

@Composable
private fun InternalDiaryTag(
	onTitle: () -> Unit,
	onTag: (String) -> Unit,
	listProvider: () -> List<TagUiState>?,
	modifier: Modifier = Modifier,
) {
	TagFlow(
		title = {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.clickable(onClick = onTitle)
					.minimumInteractiveComponentSize()
					.padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
				horizontalArrangement = Arrangement.SpaceBetween,
			) {
				Text(text = "태그")
				ChevronRightIcon()
			}
		},
		listProvider = listProvider,
		empty = { Text(text = "태그가 없어요 🐻‍❄️") },
		tag = {
			PrimaryMemoTag(
				uiState = it,
				onClick = { onTag(it.id) },
			)
		},
		modifier = modifier
			.fillMaxWidth()
			.heightIn(min = 150.dp, max = 200.dp),
	)
}

@Composable
private fun InternalDiaryColor(
	state: MemoDetailScreenState,
	modifier: Modifier = Modifier,
) {
	Row(modifier = modifier) {
		DiaryColor(
			state = state.colorState,
			modifier = Modifier
				.weight(1F)
				.height(100.dp),
		)

		Spacer(modifier = Modifier.weight(1F))
	}
}
