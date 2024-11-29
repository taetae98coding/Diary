package io.github.taetae98coding.diary.feature.buddy.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.chip.DiaryAssistChip
import io.github.taetae98coding.diary.core.design.system.icon.AccountIcon
import io.github.taetae98coding.diary.core.design.system.icon.AddIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.buddy.Buddy
import io.github.taetae98coding.diary.feature.buddy.common.BuddyBottomSheet
import io.github.taetae98coding.diary.feature.buddy.common.BuddyBottomSheetState
import io.github.taetae98coding.diary.feature.buddy.common.BuddyBottomSheetUiState

@Composable
internal fun BuddyGroup(
	state: BuddyBottomSheetState,
	buddyUiStateProvider: () -> List<Buddy>?,
	bottomSheetUiState: () -> BuddyBottomSheetUiState,
	modifier: Modifier = Modifier,
) {
	var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }

	Card(modifier = modifier) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.clickable(onClick = { isBottomSheetVisible = true })
				.minimumInteractiveComponentSize()
				.padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
		) {
			Text(text = "버디")
			AddIcon()
		}

		BuddyGroupFlowRow(
			buddyUiStateProvider = buddyUiStateProvider,
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(min = 75.dp),
		)
	}

	if (isBottomSheetVisible) {
		BuddyBottomSheet(
			state = state,
			onDismissRequest = { isBottomSheetVisible = false },
			uiStateProvider = bottomSheetUiState,
		)
	}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BuddyGroupFlowRow(
	buddyUiStateProvider: () -> List<Buddy>?,
	modifier: Modifier = Modifier,
) {
	FlowRow(
		modifier = modifier.padding(DiaryTheme.dimen.itemSpace),
		horizontalArrangement = Arrangement.spacedBy(
			space = DiaryTheme.dimen.itemSpace,
			alignment = Alignment.CenterHorizontally,
		),
		verticalArrangement = Arrangement.spacedBy(
			space = DiaryTheme.dimen.itemSpace,
			alignment = Alignment.CenterVertically,
		),
	) {
		buddyUiStateProvider().orEmpty().forEach {
			DiaryAssistChip(
				onClick = {},
				label = { Text(text = it.email) },
				leadingIcon = { AccountIcon() },
				shape = CircleShape,
			)
		}
	}
}
