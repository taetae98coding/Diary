package io.github.taetae98coding.diary.feature.buddy.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.taetae98coding.diary.core.design.system.diary.component.rememberDiaryComponentState
import io.github.taetae98coding.diary.feature.buddy.common.rememberBuddyBottomSheetState
import io.github.taetae98coding.diary.feature.buddy.detail.BuddyDetailScreenState

@Composable
internal fun rememberBuddyDetailScreenAddState(): BuddyDetailScreenState.Add {
	val coroutineScope = rememberCoroutineScope()
	val componentState = rememberDiaryComponentState()
	val buddyBottomSheetState = rememberBuddyBottomSheetState()

	return remember {
		BuddyDetailScreenState.Add(
			coroutineScope = coroutineScope,
			componentState = componentState,
			buddyBottomSheetState = buddyBottomSheetState,
		)
	}
}
