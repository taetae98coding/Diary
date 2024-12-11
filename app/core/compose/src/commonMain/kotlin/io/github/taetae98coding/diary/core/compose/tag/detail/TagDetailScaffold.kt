package io.github.taetae98coding.diary.core.compose.tag.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.compose.button.FloatingAddButton
import io.github.taetae98coding.diary.core.design.system.diary.color.DiaryColor
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponent
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.icon.DeleteIcon
import io.github.taetae98coding.diary.core.design.system.icon.FinishIcon
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun TagDetailScaffold(
	state: TagDetailScaffoldState,
	uiStateProvider: () -> TagDetailScaffoldUiState,
	modifier: Modifier = Modifier,
	titleProvider: () -> String? = { null },
	navigationIconProvider: () -> TagDetailScaffoldNavigationIcon = { TagDetailScaffoldNavigationIcon.None },
	actionsProvider: () -> TagDetailScaffoldActions = { TagDetailScaffoldActions.None },
	floatingButtonProvider: () -> TagDetailScaffoldFloatingButton = { TagDetailScaffoldFloatingButton.None },
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = { titleProvider()?.let { Text(text = it) } },
				navigationIcon = {
					when (val navigationIcon = navigationIconProvider()) {
						is TagDetailScaffoldNavigationIcon.NavigateUp -> {
							IconButton(onClick = navigationIcon.navigateUp) {
								NavigateUpIcon()
							}
						}

						is TagDetailScaffoldNavigationIcon.None -> Unit
					}
				},
				actions = {
					when (val actions = actionsProvider()) {
						is TagDetailScaffoldActions.FinishAndDelete -> {
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

						is TagDetailScaffoldActions.None -> Unit
					}
				},
			)
		},
		snackbarHost = { SnackbarHost(hostState = state.hostState) },
		floatingActionButton = {
			when (val floatingButton = floatingButtonProvider()) {
				is TagDetailScaffoldFloatingButton.Add -> {
					FloatingAddButton(
						onClick = floatingButton.add,
						progressProvider = { floatingButton.isInProgress },
					)
				}

				is TagDetailScaffoldFloatingButton.None -> Unit
			}
		},
	) {
		Content(
			state = state,
			modifier = Modifier
				.fillMaxSize()
				.padding(DiaryTheme.dimen.screenPaddingValues)
				.padding(it),
		)
	}

	HandleUiState(
		state = state,
		uiStateProvider = uiStateProvider,
	)
}

@Composable
private fun HandleUiState(
	state: TagDetailScaffoldState,
	uiStateProvider: () -> TagDetailScaffoldUiState,
) {
	val uiState = uiStateProvider()

	LaunchedEffect(
		uiState.isTitleBlankError,
		uiState.isUnknownError,
	) {
		when {
			uiState.isTitleBlankError -> {
				state.showMessage("제목을 입력해 주세요 ${Emoji.check.random()}")
				state.requestTitleFocus()
				state.titleError()
				uiState.clearState()
			}

			uiState.isUnknownError -> {
				state.showMessage("알 수 없는 에러가 발생했어요 잠시 후 다시 시도해 주세요 ${Emoji.error.random()}")
				uiState.clearState()
			}
		}
	}

	if (uiState is TagDetailScaffoldUiState.Add) {
		LaunchedEffect(
			uiState.isAddFinish,
		) {
			when {
				uiState.isAddFinish -> {
					state.showMessage("태그 추가 ${Emoji.congratulate.random()}")
					state.clearInput()
					state.requestTitleFocus()
					uiState.clearState()
				}
			}
		}
	}

	if (uiState is TagDetailScaffoldUiState.Detail) {
		LaunchedEffect(
			uiState.isUpdateFinish,
			uiState.isDeleteFinish,
		) {
			when {
				uiState.isUpdateFinish -> {
					if (state is TagDetailScaffoldState.Detail) {
						state.onUpdate()
					}
					uiState.clearState()
				}

				uiState.isDeleteFinish -> {
					if (state is TagDetailScaffoldState.Detail) {
						state.onDelete()
					}
					uiState.clearState()
				}
			}
		}
	}
}

@Composable
private fun Content(
	state: TagDetailScaffoldState,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = Modifier
			.verticalScroll(state = rememberScrollState())
			.then(modifier),
		verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
	) {
		DiaryComponent(state = state.componentState)
		Row(horizontalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace)) {
			DiaryColor(
				state = state.colorState,
				modifier = Modifier
					.weight(1F)
					.height(100.dp),
			)

			if (state is TagDetailScaffoldState.Detail) {
				Card(
					onClick = state.navigateToMemo,
					modifier = Modifier
						.weight(1F)
						.height(100.dp),
				) {
					Box(
						modifier = Modifier.fillMaxSize(),
						contentAlignment = Alignment.Center,
					) {
						Text(text = "메모")
					}
				}
			} else {
				Spacer(modifier = Modifier.weight(1F))
			}
		}
	}
}
