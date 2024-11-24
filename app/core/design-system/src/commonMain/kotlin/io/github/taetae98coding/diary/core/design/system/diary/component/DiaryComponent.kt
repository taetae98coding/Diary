package io.github.taetae98coding.diary.core.design.system.diary.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import io.github.taetae98coding.diary.core.design.system.icon.MarkdownIcon
import io.github.taetae98coding.diary.core.design.system.icon.TextFieldIcon
import io.github.taetae98coding.diary.core.design.system.text.ClearTextField
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import kotlinx.coroutines.launch

@Composable
public fun DiaryComponent(
	state: DiaryComponentState,
	modifier: Modifier = Modifier,
) {
	Card(modifier = modifier) {
		ClearTextField(
			valueProvider = { state.title },
			onValueChange = state::onTitleChange,
			modifier = Modifier.fillMaxWidth()
				.focusRequester(state.titleFocusRequester),
			label = { Text(text = "제목") },
			errorProvider = { state.isTitleError },
			singleLine = true,
		)
		DescriptionTabRow(state = state)
		DescriptionPager(
			state = state,
			modifier = Modifier.height(200.dp),
		)
	}
}

@Composable
private fun DescriptionTabRow(
	state: DiaryComponentState,
	modifier: Modifier = Modifier,
) {
	TabRow(
		selectedTabIndex = state.pagerState.currentPage,
		modifier = modifier,
	) {
		val coroutineScope = rememberCoroutineScope()

		repeat(2) { page ->
			Tab(
				selected = state.pagerState.currentPage == page,
				onClick = { coroutineScope.launch { state.pagerState.animateScrollToPage(page) } },
				icon = {
					when (page) {
						0 -> TextFieldIcon()
						1 -> MarkdownIcon()
					}
				},
			)
		}
	}
}

@Composable
private fun DescriptionPager(
	state: DiaryComponentState,
	modifier: Modifier = Modifier,
) {
	HorizontalPager(
		state = state.pagerState,
		modifier = modifier,
		key = { it },
	) { page ->
		when (page) {
			0 -> {
				ClearTextField(
					valueProvider = { state.description },
					onValueChange = state::onDescriptionChange,
					modifier = Modifier.fillMaxSize(),
					label = { Text(text = "설명") },
				)
			}

			1 -> {
				Column(
					modifier = Modifier.fillMaxSize()
						.verticalScroll(rememberScrollState())
						.padding(DiaryTheme.dimen.diaryPaddingValues),
				) {
					Markdown(
						content = state.description,
						modifier = Modifier.fillMaxSize(),
					)
				}
			}
		}
	}
}
