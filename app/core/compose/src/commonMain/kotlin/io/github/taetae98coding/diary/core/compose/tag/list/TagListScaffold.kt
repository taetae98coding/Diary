package io.github.taetae98coding.diary.core.compose.tag.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import io.github.taetae98coding.diary.core.compose.error.NetworkError
import io.github.taetae98coding.diary.core.compose.error.UnknownError
import io.github.taetae98coding.diary.core.compose.swipe.FinishAndDeleteSwipeBox
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun TagListScaffold(
	state: TagListScaffoldState,
	uiStateProvider: () -> TagListScaffoldUiState,
	listUiStateProvider: () -> TagListUiState,
	modifier: Modifier = Modifier,
	floatingButtonProvider: () -> TagListScaffoldFloatingButton = { TagListScaffoldFloatingButton.None },
) {
	Scaffold(
		modifier = modifier,
		topBar = { TopAppBar(title = { Text(text = "태그") }) },
		snackbarHost = { SnackbarHost(hostState = state.hostState) },
		floatingActionButton = {
			when (val floatingButton = floatingButtonProvider()) {
				is TagListScaffoldFloatingButton.Add -> {
					FloatingAddButton(onClick = floatingButton.add)
				}

				is TagListScaffoldFloatingButton.None -> Unit
			}
		},
	) {
		Content(
			state = state,
			listUiStateProvider = listUiStateProvider,
			modifier = Modifier
				.fillMaxSize()
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
	state: TagListScaffoldState,
	uiStateProvider: () -> TagListScaffoldUiState,
) {
	val uiState = uiStateProvider()

	LaunchedEffect(
		uiState.isTagFinish,
		uiState.isTagDelete,
		uiState.isNetworkError,
		uiState.isUnknownError,
	) {
		when {
			uiState.isTagFinish -> {
				state.showMessage(
					message = "태그 완료 ${Emoji.congratulate.random()}",
					actionLabel = "취소",
				) {
					uiState.restart.value()
				}
				uiState.clearState()
			}

			uiState.isTagDelete -> {
				state.showMessage(
					message = "태그 삭제 ${Emoji.congratulate.random()}",
					actionLabel = "취소",
				) {
					uiState.restore.value()
				}
				uiState.clearState()
			}

			uiState.isNetworkError -> state.showMessage("네트워크 상태를 확인해 주세요 ${Emoji.check.random()}")

			uiState.isUnknownError -> {
				state.showMessage("알 수 없는 에러가 발생했어요 잠시 후 다시 시도해 주세요 ${Emoji.error.random()}")
				uiState.clearState()
			}
		}
	}
}

@Composable
private fun Content(
	state: TagListScaffoldState,
	listUiStateProvider: () -> TagListUiState,
	modifier: Modifier = Modifier,
) {
	LazyColumn(
		modifier = modifier,
		contentPadding = DiaryTheme.dimen.screenPaddingValues,
		verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
	) {
		when (val uiState = listUiStateProvider()) {
			is TagListUiState.Loading -> {
				items(
					count = 5,
					contentType = { "Tag" },
				) {
					TagItem(
						uiState = null,
						onClick = { },
						modifier = Modifier.animateItem(),
					)
				}
			}

			is TagListUiState.NetworkError -> {
				item(
					key = "NetworkError",
					contentType = "NetworkError",
				) {
					NetworkError(
						onRetry = uiState.retry,
						modifier = Modifier
							.fillParentMaxSize()
							.animateItem(),
					)
				}
			}

			is TagListUiState.UnknownError -> {
				item(
					key = "UnknownError",
					contentType = "UnknownError",
				) {
					UnknownError(
						onRetry = uiState.retry,
						modifier = Modifier
							.fillParentMaxSize()
							.animateItem(),
					)
				}
			}

			is TagListUiState.State -> {
				if (uiState.list.isEmpty()) {
					item(
						key = "Empty",
						contentType = "Empty",
					) {
						Box(
							modifier = Modifier
								.fillParentMaxSize()
								.animateItem(),
							contentAlignment = Alignment.Center,
						) {
							Text(
								text = "태그가 없어요 🐼",
								style = DiaryTheme.typography.headlineMedium,
							)
						}
					}
				} else {
					items(
						items = uiState.list,
						key = { it.id },
						contentType = { "Tag" },
					) {
						TagItem(
							uiState = it,
							onClick = { state.navigateToTag(it.id) },
							modifier = Modifier.animateItem(),
						)
					}
				}
			}
		}
	}
}

@Composable
private fun TagItem(
	uiState: TagListItemUiState?,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	FinishAndDeleteSwipeBox(
		modifier = modifier,
		onFinish = { uiState?.finish?.value?.invoke() },
		onDelete = { uiState?.delete?.value?.invoke() },
	) {
		Card(onClick = onClick) {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.padding(16.dp),
				contentAlignment = Alignment.Center,
			) {
				Text(
					text = uiState?.title.orEmpty(),
					style = DiaryTheme.typography.titleLarge,
				)
			}
		}
	}
}
