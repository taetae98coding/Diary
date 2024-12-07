package io.github.taetae98coding.diary.feature.buddy.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
internal fun rememberBuddyBottomSheetState(
	initialEmail: String = "",
): BuddyBottomSheetState = rememberSaveable(saver = BuddyBottomSheetState.saver()) {
	BuddyBottomSheetState(
		initialEmail = initialEmail,
	)
}
