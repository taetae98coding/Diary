package io.github.taetae98coding.diary.feature.buddy.detail

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.taetae98coding.diary.core.design.system.diary.component.rememberDiaryComponentState
import io.github.taetae98coding.diary.feature.buddy.common.rememberBuddyBottomSheetState

@Composable
internal fun rememberBuddyDetailScreenDetailState(
	onTag: () -> Unit,
	onCalendar: () -> Unit,
): BuddyDetailScreenState.Detail {
	val coroutineScope = rememberCoroutineScope()
	val drawerState = rememberDrawerState(DrawerValue.Closed)
	val componentState = rememberDiaryComponentState()
	val buddyBottomSheetState = rememberBuddyBottomSheetState()

	return remember {
		BuddyDetailScreenState.Detail(
			onTag = onTag,
			onCalendar = onCalendar,
			coroutineScope = coroutineScope,
			drawerState = drawerState,
			componentState = componentState,
			buddyBottomSheetState = buddyBottomSheetState,
		)
	}
}
