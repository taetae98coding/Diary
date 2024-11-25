package io.github.taetae98coding.diary.feature.tag.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.icon.DeleteIcon
import io.github.taetae98coding.diary.core.design.system.icon.FinishIcon
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TagDetailScreen(
	state: TagDetailScreenState,
	titleProvider: () -> String?,
	navigateButtonProvider: () -> TagDetailNavigationButton,
	actionButtonProvider: () -> TagDetailActionButton,
	floatingButtonProvider: () -> TagDetailFloatingButton,
	uiStateProvider: () -> TagDetailScreenUiState,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = { titleProvider()?.let { Text(text = it) } },
				navigationIcon = {
					when (val button = navigateButtonProvider()) {
						is TagDetailNavigationButton.NavigateUp -> {
							IconButton(onClick = button.onNavigateUp) {
								NavigateUpIcon()
							}
						}

						is TagDetailNavigationButton.None -> Unit
					}
				},
				actions = {
					val button = actionButtonProvider()

					AnimatedVisibility(
						visible = button is TagDetailActionButton.FinishAndDetail,
						enter = fadeIn(),
						exit = fadeOut(),
					) {
						val isFinish = if (button is TagDetailActionButton.FinishAndDetail) {
							button.isFinish
						} else {
							false
						}

						IconToggleButton(
							checked = isFinish,
							onCheckedChange = {
								if (button is TagDetailActionButton.FinishAndDetail) {
									button.onFinishChange(it)
								}
							},
						) {
							FinishIcon()
						}
					}

					AnimatedVisibility(
						visible = button is TagDetailActionButton.FinishAndDetail,
						enter = fadeIn(),
						exit = fadeOut(),
					) {
						IconButton(
							onClick = {
								if (button is TagDetailActionButton.FinishAndDetail) {
									button.delete()
								}
							},
						) {
							DeleteIcon()
						}
					}
				},
			)
		},
		snackbarHost = { SnackbarHost(hostState = state.hostState) },
		floatingActionButton = {
			val button = floatingButtonProvider()

			AnimatedVisibility(
				visible = button is TagDetailFloatingButton.Add,
				enter = scaleIn(),
				exit = scaleOut(),
			) {
				val isProgress by remember { derivedStateOf { uiStateProvider().isProgress } }

				FloatingAddButton(
					onClick = {
						if (button is TagDetailFloatingButton.Add) {
							button.onAdd()
						}
					},
					progressProvider = { isProgress },
				)
			}
		},
	) {
		Content(
			state = state,
			modifier = Modifier.fillMaxSize()
				.padding(DiaryTheme.dimen.screenPaddingValues)
				.padding(it),
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
	state: TagDetailScreenState,
	uiStateProvider: () -> TagDetailScreenUiState,
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
				state.showMessage("태그 추가 ${Emoji.congratulate.random()}")
				state.clearInput()
				state.requestTitleFocus()
			}

			uiState.isDelete -> {
				if (state is TagDetailScreenState.Detail) {
					state.onDelete()
				}
			}

			uiState.isUpdate -> {
				if (state is TagDetailScreenState.Detail) {
					state.onUpdate()
				}
			}

			uiState.isTitleBlankError -> {
				state.showMessage("제목을 입력해 주세요 ${Emoji.check.random()}")
				state.requestTitleFocus()
				state.titleError()
			}

			uiState.isUnknownError -> state.showMessage("알 수 없는 에러가 발생했어요 잠시 후 다시 시도해 주세요 ${Emoji.error.random()}")
		}

		uiState.onMessageShow()
	}
}

@Composable
private fun LaunchedFocus(
	state: TagDetailScreenState,
) {
	LaunchedEffect(state) {
		if (state is TagDetailScreenState.Add) {
			state.requestTitleFocus()
		}
	}
}

@Composable
private fun Content(
	state: TagDetailScreenState,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = Modifier.verticalScroll(state = rememberScrollState())
			.then(modifier),
		verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
	) {
		DiaryComponent(state = state.componentState)
		Row {
			DiaryColor(
				state = state.colorState,
				modifier = Modifier.weight(1F)
					.height(100.dp),
			)

			Spacer(modifier = Modifier.weight(1F))
		}
	}
}
