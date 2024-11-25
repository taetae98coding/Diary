package io.github.taetae98coding.diary.core.design.system.diary.date

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import io.github.taetae98coding.diary.core.design.system.date.DiaryDatePickerDialog
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import kotlinx.datetime.LocalDate

@Composable
public fun DiaryDate(
	state: DiaryDateState,
	modifier: Modifier = Modifier,
) {
	Card(modifier = modifier) {
		val itemModifier = Modifier.fillMaxWidth()

		Title(
			state = state,
			modifier = itemModifier.padding(DiaryTheme.dimen.diaryPaddingValues),
		)
		AnimatedVisibility(
			visible = state.hasDate,
			modifier = itemModifier,
		) {
			Date(state = state)
		}
	}
}

@Composable
private fun Title(
	state: DiaryDateState,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = Modifier
			.toggleable(value = state.hasDate, onValueChange = state::onHasDateChange)
			.then(modifier),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(text = "날짜")
		Switch(
			checked = state.hasDate,
			onCheckedChange = null,
		)
	}
}

@Composable
private fun Date(
	state: DiaryDateState,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
		verticalAlignment = Alignment.CenterVertically,
	) {
		DateButton(
			dateProvider = { state.start },
			onSelectDate = state::onStartChange,
			modifier = Modifier.weight(1F),
		)
		Text(text = " ~ ")
		DateButton(
			dateProvider = { state.endInclusive },
			onSelectDate = state::onEndInclusiveChange,
			modifier = Modifier.weight(1F),
		)
	}
}

@Composable
private fun DateButton(
	dateProvider: () -> LocalDate,
	onSelectDate: (LocalDate) -> Unit,
	modifier: Modifier = Modifier,
) {
	var isPickerVisible by rememberSaveable { mutableStateOf(false) }

	TextButton(
		onClick = { isPickerVisible = true },
		modifier = modifier,
	) {
		AnimatedContent(
			targetState = dateProvider(),
			transitionSpec = {
				if (targetState > initialState) {
					(slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
				} else {
					(slideInVertically { -it } + fadeIn()) togetherWith (slideOutVertically { it } + fadeOut())
				}
			},
		) { target ->
			Column(horizontalAlignment = Alignment.CenterHorizontally) {
				Text(
					text = target.year.toString(),
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center,
					style = DiaryTheme.typography.labelSmall,
				)
				Text(
					text = "${target.monthNumber}월 ${target.dayOfMonth}일",
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center,
					style = DiaryTheme.typography.labelLarge,
				)
			}
		}
	}

	if (isPickerVisible) {
		DiaryDatePickerDialog(
			localDate = dateProvider(),
			onConfirm = onSelectDate,
			onDismissRequest = { isPickerVisible = false },
		)
	}
}
