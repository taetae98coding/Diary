package io.github.taetae98coding.diary.feature.calendar.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.chip.DiaryFilterChip
import io.github.taetae98coding.diary.core.design.system.icon.TagIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CalendarFilterDialog(
	onDismissRequest: () -> Unit,
	listProvider: () -> List<TagUiState>?,
	modifier: Modifier = Modifier,
) {
	BasicAlertDialog(
		onDismissRequest = onDismissRequest,
		modifier = modifier,
	) {
		Card(modifier = Modifier.height(IntrinsicSize.Min)) {
			Title()
			Content(
				listProvider = listProvider,
				modifier = Modifier
					.fillMaxWidth()
					.heightIn(min = 100.dp),
			)
		}
	}
}

@Composable
private fun Title(
	modifier: Modifier = Modifier,
) {
	Text(
		text = "태그",
		modifier = modifier
			.minimumInteractiveComponentSize()
			.padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
	)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Content(
	listProvider: () -> List<TagUiState>?,
	modifier: Modifier = Modifier,
) {
	val isLoading by remember { derivedStateOf { listProvider() == null } }
	val isEmpty by remember { derivedStateOf { !isLoading && listProvider().isNullOrEmpty() } }

	if (isLoading) {
		Box(
			modifier = modifier,
			contentAlignment = Alignment.Center,
		) {
			CircularProgressIndicator()
		}
	} else if (isEmpty) {
		Box(
			modifier = modifier,
			contentAlignment = Alignment.Center,
		) {
			Text("태그가 없어요 🦊")
		}
	} else {
		FlowRow(
			modifier = modifier
				.verticalScroll(rememberScrollState())
				.padding(DiaryTheme.dimen.itemSpace),
			horizontalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace, Alignment.CenterHorizontally),
			verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace, Alignment.CenterVertically),
		) {
			listProvider()?.forEach {
				DiaryFilterChip(
					selected = it.isSelected,
					onClick = {
						if (it.isSelected) {
							it.unselect.value()
						} else {
							it.select.value()
						}
					},
					label = { Text(text = it.title) },
					leadingIcon = { TagIcon() },
					shape = CircleShape,
				)
			}
		}
	}
}
