package io.github.taetae98coding.diary.feature.buddy.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.compose.error.NetworkError
import io.github.taetae98coding.diary.core.compose.error.UnknownError
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BuddyListScreen(
	listUiStateProvider: () -> BuddyListUiState,
	onGroup: (String) -> Unit,
	modifier: Modifier = Modifier,
	floatingActionButton: @Composable () -> Unit = {},
) {
	Scaffold(
		modifier = modifier,
		topBar = { TopAppBar(title = { Text(text = "버디") }) },
		floatingActionButton = floatingActionButton,
	) {
		Content(
			listUiStateProvider = listUiStateProvider,
			onGroup = onGroup,
			modifier = Modifier
				.fillMaxSize()
				.padding(it),
		)
	}
}

@Composable
private fun Content(
	listUiStateProvider: () -> BuddyListUiState,
	onGroup: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	LazyColumn(
		modifier = modifier,
		contentPadding = DiaryTheme.dimen.screenPaddingValues,
		verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
	) {
		when (val uiState = listUiStateProvider()) {
			is BuddyListUiState.Loading -> {
				items(
					count = 5,
					contentType = { "BuddyGroup" },
				) {
					BuddyListItem(
						group = null,
						onClick = {},
						modifier = Modifier.animateItem(),
					)
				}
			}

			is BuddyListUiState.NetworkError -> {
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

			is BuddyListUiState.UnknownError -> {
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

			is BuddyListUiState.State -> {
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
								text = "그룹이 없어요 🐭",
								style = DiaryTheme.typography.headlineMedium,
							)
						}
					}
				} else {
					items(
						items = uiState.list,
						key = { it.id },
						contentType = { "BuddyGroup" },
					) {
						BuddyListItem(
							group = it,
							onClick = { onGroup(it.id) },
							modifier = Modifier.animateItem(),
						)
					}
				}
			}

			is BuddyListUiState.ZZ -> {
				item(
					key = "ZZ",
					contentType = "ZZ",
				) {
					Box(
						modifier = Modifier
							.fillParentMaxSize()
							.animateItem(),
						contentAlignment = Alignment.Center,
					) {
						Text(
							text = uiState.throwable.toString(),
						)
					}
				}
			}
		}
	}
}

@Composable
private fun BuddyListItem(
	group: BuddyGroup?,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Card(
		onClick = onClick,
		modifier = modifier,
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			contentAlignment = Alignment.Center,
		) {
			Text(
				text = group?.detail?.title.orEmpty(),
				style = DiaryTheme.typography.titleLarge,
			)
		}
	}
}
