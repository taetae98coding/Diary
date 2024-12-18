package io.github.taetae98coding.diary.core.compose.tag.memo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.compose.button.FloatingAddButton
import io.github.taetae98coding.diary.core.compose.error.NetworkError
import io.github.taetae98coding.diary.core.compose.error.UnknownError
import io.github.taetae98coding.diary.core.compose.memo.list.MemoListItem
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun TagMemoScaffold(
	state: TagMemoScaffoldState,
	uiStateProvider: () -> TagMemoScaffoldUiState,
	listUiStateProvider: () -> TagMemoListUiState,
	onAdd: () -> Unit,
	onMemo: (String) -> Unit,
	modifier: Modifier = Modifier,
	navigateIconProvider: () -> TagMemoNavigateIcon = { TagMemoNavigateIcon.None },
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = {},
				navigationIcon = {
					when (val navigateIcon = navigateIconProvider()) {
						is TagMemoNavigateIcon.NavigateUp -> {
							IconButton(onClick = navigateIcon.navigateUp) {
								NavigateUpIcon()
							}
						}

						is TagMemoNavigateIcon.None -> Unit
					}
				},
			)
		},
		snackbarHost = { SnackbarHost(hostState = state.hostState) },
		floatingActionButton = { FloatingAddButton(onClick = onAdd) },
	) {
		Content(
			listUiStateProvider = listUiStateProvider,
			onMemo = onMemo,
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
	state: TagMemoScaffoldState,
	uiStateProvider: () -> TagMemoScaffoldUiState,
) {
	val uiState = uiStateProvider()

	LaunchedEffect(
		uiState.isFinish,
		uiState.isDelete,
		uiState.isUnknownError,
	) {
		when {
			uiState.isFinish -> {
				state.showMessage(
					message = "메모 완료 ${Emoji.congratulate.random()}",
					actionLabel = "취소",
				) {
					uiState.restartTag()
				}
				uiState.clearState()
			}

			uiState.isDelete -> {
				state.showMessage(
					message = "메모 삭제 ${Emoji.congratulate.random()}",
					actionLabel = "취소",
				) {
					uiState.restoreTag()
				}
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
private fun Content(
	listUiStateProvider: () -> TagMemoListUiState,
	onMemo: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	LazyColumn(
		modifier = modifier,
		contentPadding = DiaryTheme.dimen.screenPaddingValues,
		verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
	) {
		val listUiState = listUiStateProvider()

		if (listUiState is TagMemoListUiState.Loading) {
			items(
				count = 5,
				contentType = { "Memo" },
			) {
				MemoListItem(
					uiState = null,
					onClick = {},
					modifier = Modifier.animateItem(),
				)
			}
		} else if (listUiState is TagMemoListUiState.State) {
			if (listUiState.list.isEmpty()) {
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
							text = "메모가 없어요 🐰",
							style = DiaryTheme.typography.headlineMedium,
						)
					}
				}
			} else {
				items(
					items = listUiState.list,
					key = { it.id },
					contentType = { "Memo" },
				) {
					MemoListItem(
						uiState = it,
						onClick = { onMemo(it.id) },
						modifier = Modifier.animateItem(),
					)
				}
			}
		} else if (listUiState is TagMemoListUiState.NetworkError) {
			item(
				key = "NetworkError",
				contentType = "NetworkError",
			) {
				NetworkError(
					onRetry = listUiState.retry,
					modifier = Modifier
						.fillParentMaxSize()
						.animateItem(),
				)
			}
		} else if (listUiState is TagMemoListUiState.UnknownError) {
			item(
				key = "UnknownError",
				contentType = "UnknownError",
			) {
				UnknownError(
					onRetry = listUiState.retry,
					modifier = Modifier
						.fillParentMaxSize()
						.animateItem(),
				)
			}
		}
	}
}
