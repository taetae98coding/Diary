package io.github.taetae98coding.diary.core.compose.memo.detail

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.compose.button.FloatingAddButton
import io.github.taetae98coding.diary.core.compose.tag.card.PrimaryTagCardItem
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardFlow
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardUiState
import io.github.taetae98coding.diary.core.compose.topbar.TopBarTitle
import io.github.taetae98coding.diary.core.design.system.diary.color.DiaryColor
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponent
import io.github.taetae98coding.diary.core.design.system.diary.date.DiaryDate
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.icon.ChevronRightIcon
import io.github.taetae98coding.diary.core.design.system.icon.DeleteIcon
import io.github.taetae98coding.diary.core.design.system.icon.FinishIcon
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MemoDetailScaffold(
	state: MemoDetailScaffoldState,
	uiStateProvider: () -> MemoDetailScaffoldUiState,
	primaryTagUiStateProvider: () -> TagCardUiState,
	onTagTitle: () -> Unit,
	onTag: (String) -> Unit,
	modifier: Modifier = Modifier,
	titleProvider: () -> String? = { null },
	navigationIconProvider: () -> MemoDetailScaffoldNavigationIcon = { MemoDetailScaffoldNavigationIcon.None },
	actionsProvider: () -> MemoDetailScaffoldActions = { MemoDetailScaffoldActions.None },
	floatingButtonProvider: () -> MemoDetailScaffoldFloatingButton = { MemoDetailScaffoldFloatingButton.None },
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = { titleProvider()?.let { TopBarTitle(text = it) } },
				navigationIcon = {
					when (val navigationIcon = navigationIconProvider()) {
						is MemoDetailScaffoldNavigationIcon.NavigateUp -> {
							IconButton(onClick = navigationIcon.navigateUp) {
								NavigateUpIcon()
							}
						}

						is MemoDetailScaffoldNavigationIcon.None -> Unit
					}
				},
				actions = {
					when (val actions = actionsProvider()) {
						is MemoDetailScaffoldActions.FinishAndDelete -> {
							IconToggleButton(
								checked = actions.isFinish,
								onCheckedChange = { isFinish ->
									if (isFinish) {
										actions.finish()
									} else {
										actions.restart()
									}
								},
							) {
								FinishIcon()
							}

							IconButton(onClick = actions.delete) {
								DeleteIcon()
							}
						}

						is MemoDetailScaffoldActions.None -> Unit
					}
				},
			)
		},
		snackbarHost = { SnackbarHost(hostState = state.hostState) },
		floatingActionButton = {
			when (val floatingButton = floatingButtonProvider()) {
				is MemoDetailScaffoldFloatingButton.Add -> {
					FloatingAddButton(
						onClick = floatingButton.add,
						progressProvider = { floatingButton.isInProgress },
					)
				}

				is MemoDetailScaffoldFloatingButton.None -> Unit
			}
		},
	) {
		Content(
			onTagTitle = onTagTitle,
			onTag = onTag,
			state = state,
			primaryTagUiStateProvider = primaryTagUiStateProvider,
			modifier = Modifier
				.fillMaxSize()
				.padding(it)
				.padding(DiaryTheme.dimen.screenPaddingValues),
		)
	}

	HandleUiState(
		state = state,
		uiStateProvider = uiStateProvider,
	)

	LaunchedFocus(state = state)
}

@Composable
private fun HandleUiState(
	state: MemoDetailScaffoldState,
	uiStateProvider: () -> MemoDetailScaffoldUiState,
) {
	val uiState = uiStateProvider()

	if (uiState is MemoDetailScaffoldUiState.Add) {
		LaunchedEffect(
			uiState.isAddInProgress,
		) {
			when {
				uiState.isAddInProgress -> {
					state.showMessage("메모 추가 ${Emoji.congratulate.random()}")
					state.clearInput()
					state.requestTitleFocus()
					uiState.clearState()
				}
			}
		}
	}

	if (uiState is MemoDetailScaffoldUiState.Detail) {
		LaunchedEffect(
			uiState.isUpdateFinish,
			uiState.isUpdateFail,
			uiState.isDeleteFinish,
		) {
			when {
				uiState.isUpdateFinish -> {
					if (state is MemoDetailScaffoldState.Detail) {
						state.onUpdate()
					}
					uiState.clearState()
				}

				uiState.isUpdateFail -> {
					if (state is MemoDetailScaffoldState.Detail) {
						state.showMessage("수정 실패했습니다", "무시하고 뒤로가기", state.onUpdate)
					}
					uiState.clearState()
				}

				uiState.isDeleteFinish -> {
					if (state is MemoDetailScaffoldState.Detail) {
						state.onDelete()
					}
					uiState.clearState()
				}
			}
		}
	}

	LaunchedEffect(
		uiState.isTitleBlankError,
		uiState.isNetworkError,
		uiState.isUnknownError,
	) {
		when {
			uiState.isTitleBlankError -> {
				state.showMessage("제목을 입력해 주세요 ${Emoji.check.random()}")
				state.titleError()
				uiState.clearState()
			}

			uiState.isNetworkError -> {
				state.showMessage("네트워크 연결을 확인해 주세요 ${Emoji.check.random()}")
				uiState.clearState()
			}

			uiState.isUnknownError -> {
				state.showMessage("알 수 없는 에러가 발생했어요 잠시 후 다시 시도해 주세요 ${Emoji.error.random()}")
				uiState.clearState()
			}
		}
	}
}

@Composable
private fun LaunchedFocus(
	state: MemoDetailScaffoldState,
) {
	LaunchedEffect(state) {
		if (state is MemoDetailScaffoldState.Add) {
			state.requestTitleFocus()
		}
	}
}

@Composable
private fun Content(
	onTagTitle: () -> Unit,
	onTag: (String) -> Unit,
	state: MemoDetailScaffoldState,
	primaryTagUiStateProvider: () -> TagCardUiState,
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
			onTagTitle = onTagTitle,
			onTag = onTag,
			state = state,
			primaryTagUiStateProvider = primaryTagUiStateProvider,
		)
		InternalDiaryColor(state = state)
	}
}

@Composable
private fun InternalDiaryTag(
	onTagTitle: () -> Unit,
	onTag: (String) -> Unit,
	state: MemoDetailScaffoldState,
	primaryTagUiStateProvider: () -> TagCardUiState,
	modifier: Modifier = Modifier,
) {
	TagCardFlow(
		title = {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.clickable(onClick = onTagTitle)
					.minimumInteractiveComponentSize()
					.padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(text = "태그")
				ChevronRightIcon()
			}
		},
		uiStateProvider = primaryTagUiStateProvider,
		empty = { Text(text = "태그가 없어요 🐻‍❄️") },
		tag = {
			PrimaryTagCardItem(
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
	state: MemoDetailScaffoldState,
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
