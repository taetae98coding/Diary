package io.github.taetae98coding.diary.core.compose.memo.tag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.compose.tag.card.PrimaryTagCardItem
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardFlow
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardItem
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardUiState
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MemoTagScaffold(
	onTagAdd: () -> Unit,
	primaryTagUiStateProvider: () -> TagCardUiState,
	tagUiStateProvider: () -> TagCardUiState,
	modifier: Modifier = Modifier,
	navigationIcon: () -> MemoTagNavigationIcon,
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = { Text(text = "메모 태그") },
				navigationIcon = {
					when (val navigationIcon = navigationIcon()) {
						is MemoTagNavigationIcon.NavigateUp -> {
							IconButton(onClick = navigationIcon.navigateUp) {
								NavigateUpIcon()
							}
						}

						is MemoTagNavigationIcon.None -> Unit
					}
				},
			)
		},
	) {
		Content(
			onTagAdd = onTagAdd,
			primaryTagUiStateProvider = primaryTagUiStateProvider,
			tagUiStateProvider = tagUiStateProvider,
			modifier = Modifier
				.fillMaxSize()
				.padding(it)
				.padding(DiaryTheme.dimen.screenPaddingValues),
		)
	}
}

@Composable
private fun Content(
	onTagAdd: () -> Unit,
	primaryTagUiStateProvider: () -> TagCardUiState,
	tagUiStateProvider: () -> TagCardUiState,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
	) {
		TagCardFlow(
			modifier = Modifier
				.fillMaxWidth()
				.weight(3F),
			uiStateProvider = primaryTagUiStateProvider,
			title = {
				Text(
					text = "캘린더 태그",
					modifier = Modifier
						.fillMaxWidth()
						.minimumInteractiveComponentSize()
						.padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
				)
			},
			empty = { Text(text = "태그가 없어요 🐻") },
			tag = {
				PrimaryTagCardItem(uiState = it)
			},
		)

		TagCardFlow(
			modifier = Modifier
				.fillMaxWidth()
				.weight(7F),
			uiStateProvider = tagUiStateProvider,
			title = {
				Text(
					text = "태그",
					modifier = Modifier
						.fillMaxWidth()
						.minimumInteractiveComponentSize()
						.padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
				)
			},
			empty = {
				Button(onClick = onTagAdd) {
					Text(text = "태그 추가하러 가기")
				}
			},
			tag = { TagCardItem(uiState = it) },
		)
	}
}
