package io.github.taetae98coding.diary.core.design.system.dimen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

public data class DiaryDimen(
	val screenHorizontalPadding: Dp = 12.dp,
	val screenVerticalPadding: Dp = 12.dp,
	val diaryHorizontalPadding: Dp = 16.dp,
	val diaryVerticalPadding: Dp = 8.dp,
	val itemSpace: Dp = 4.dp,
) {
	val screenPaddingValues: PaddingValues =
		PaddingValues(
			horizontal = screenHorizontalPadding,
			vertical = screenVerticalPadding,
		)

	val diaryPaddingValues: PaddingValues =
		PaddingValues(
			horizontal = diaryHorizontalPadding,
			vertical = diaryVerticalPadding,
		)
}
