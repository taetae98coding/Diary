package io.github.taetae98coding.diary.feature.buddy.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue

internal class BuddyBottomSheetState(
	initialEmail: String,
) {
	var email by mutableStateOf(initialEmail)
		private set

	fun onEmailChange(value: String) {
		email = value
	}

	companion object {
		fun saver(): Saver<BuddyBottomSheetState, Any> = listSaver(
			save = { listOf(it.email) },
			restore = {
				BuddyBottomSheetState(it[0])
			},
		)
	}
}
