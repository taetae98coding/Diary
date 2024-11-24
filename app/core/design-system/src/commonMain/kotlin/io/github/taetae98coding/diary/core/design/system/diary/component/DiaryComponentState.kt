package io.github.taetae98coding.diary.core.design.system.diary.component

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester

public class DiaryComponentState internal constructor(initialTitle: String, initialDescription: String) {
	internal var isTitleError by mutableStateOf(false)
		private set
	public var title: String by mutableStateOf(initialTitle)
		private set
	public var description: String by mutableStateOf(initialDescription)
		private set

	internal val pagerState =
		PagerState(
			currentPage =
			if (initialDescription.isBlank()) {
				0
			} else {
				1
			},
		) {
			2
		}
	internal val titleFocusRequester = FocusRequester()

	internal fun onTitleChange(value: String) {
		title = value
		if (value.isNotBlank()) {
			isTitleError = false
		}
	}

	internal fun onDescriptionChange(value: String) {
		description = value
	}

	public fun requestTitleFocus() {
		titleFocusRequester.requestFocus()
	}

	public fun titleError() {
		isTitleError = true
	}

	public fun clearInput() {
		isTitleError = false
		title = ""
		description = ""
	}

	public companion object {
		internal fun saver(): Saver<DiaryComponentState, Any> =
			listSaver(
				save = { listOf(it.title, it.description) },
				restore = {
					DiaryComponentState(
						initialTitle = it[0],
						initialDescription = it[1],
					)
				},
			)
	}
}
