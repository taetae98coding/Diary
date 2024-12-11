package io.github.taetae98coding.diary.core.compose.tag.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.compose.error.NetworkError
import io.github.taetae98coding.diary.core.compose.error.UnknownError
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@Composable
public fun TagCardFlow(
	title: @Composable () -> Unit,
	uiStateProvider: () -> TagCardUiState,
	empty: @Composable () -> Unit,
	tag: @Composable (TagCardItemUiState) -> Unit,
	modifier: Modifier = Modifier,
) {
	Card(modifier = modifier.height(IntrinsicSize.Min)) {
		title()
		Content(
			uiStateProvider = uiStateProvider,
			empty = empty,
			tag = tag,
		)
	}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Content(
	uiStateProvider: () -> TagCardUiState,
	empty: @Composable () -> Unit,
	tag: @Composable (TagCardItemUiState) -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(modifier = modifier) {
		val uiState = uiStateProvider()

		AnimatedVisibility(
			visible = uiState is TagCardUiState.Loading,
			enter = fadeIn(),
			exit = fadeOut(),
		) {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator()
			}
		}

		AnimatedVisibility(
			visible = uiState is TagCardUiState.State && uiState.list.isEmpty(),
			enter = fadeIn(),
			exit = fadeOut(),
		) {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center,
			) {
				empty()
			}
		}

		AnimatedVisibility(
			visible = uiState is TagCardUiState.State && uiState.list.isNotEmpty(),
			enter = fadeIn(),
			exit = fadeOut(),
		) {
			FlowRow(
				modifier = Modifier
					.fillMaxSize()
					.verticalScroll(rememberScrollState())
					.padding(DiaryTheme.dimen.itemSpace),
				horizontalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace, Alignment.CenterHorizontally),
				verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace, Alignment.CenterVertically),
			) {
				if (uiState is TagCardUiState.State) {
					uiState.list.forEach {
						key(it.id) {
							tag(it)
						}
					}
				}
			}
		}

		AnimatedVisibility(
			visible = uiState is TagCardUiState.NetworkError,
			enter = fadeIn(),
			exit = fadeOut(),
		) {
			NetworkError(
				onRetry = {
					if (uiState is TagCardUiState.NetworkError) {
						uiState.refresh()
					}
				},
				modifier = Modifier.fillMaxSize(),
			)
		}

		AnimatedVisibility(
			visible = uiState is TagCardUiState.UnknownError,
			enter = fadeIn(),
			exit = fadeOut(),
		) {
			UnknownError(
				onRetry = {
					if (uiState is TagCardUiState.UnknownError) {
						uiState.refresh()
					}
				},
				modifier = Modifier.fillMaxSize(),
			)
		}
	}
}
